import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { AngularFireAuth } from "@angular/fire/compat/auth";
import firebase from 'firebase/compat/app';
import { environment } from '../../environments/environment';
import { ROLE_USER, ROLE_ADMIN } from '../app-constants';


@Injectable({
  providedIn: 'root'
})
export class MsptFirebaseAuthService {

  // the key name in the local storage where we store login status
  private readonly USER_LOGGED_IN = 'MSPT_DEMO_APP_FIREBASE_USER_LOGGED_IN';

  // the key name in the local storage where we store the JWT token
  private readonly JWT_TOKEN = 'MSPT_DEMO_APP_JWT_TOKEN';

  // store the requested/attempted URL so we can redirect after successful logging in
  public redirectUrl: string = '';

  // it stores the authenticated firebase user, otherwise stores null value.
  private userSubject;
   // an observable which is based on the userSubject subject
  private userObservable;

  // it stores the current value of the user role.
  private userRoleSubject;
  // an observable which is based on the userRoleSubject subject
  private userRoleObservable;

  // it stores and provides the loggedIn flag
  private userLoggedInSubject;
  // an observable which is based on the userLoggedInSubject subject
  private userLoggedInObservable;

  // subscription for firebase authState
  private firebaseAuthStateSubscription: Subscription;

  /**
   * Constructor
   */
  constructor(private afAuth: AngularFireAuth)
  {
    // Initiate the userLoggedInSubject from the value stored in the local storage
    this.userLoggedInSubject = new BehaviorSubject<boolean>( this.readUserLoggedInFlag() );
    //
    // Initiate the userSubject with null as default
    this.userSubject = new BehaviorSubject<firebase.User | null>(null);
    //
    // Initiate the userRoleSubject with ROLE_USER as default
    this.userRoleSubject = new BehaviorSubject(ROLE_USER);
    //
    // create observable to conceal the subject from the code that uses it.
    this.userLoggedInObservable = this.userLoggedInSubject.asObservable();
    this.userObservable = this.userSubject.asObservable();
    this.userRoleObservable = this.userRoleSubject.asObservable();

    // Firebase stores authentication info in the local storage.
    // let's subscribe for firebase authstate to check if the user is currently authenticated
    // Firebase makes request to the Firebase backend to check auth status
    this.firebaseAuthStateSubscription = this.afAuth.authState.subscribe(user => {
      if (user) {
        console.log('User is authenticated with ' + user.email);
        // get the JWT token used to identify the user to a Firebase service
        user?.getIdToken().then(token =>{
          this.storeJwtToken(token);
        });
        // set user object
        this.setUser(user);
        // user is authenticated, so we set the loggedIn flag
        this.setUserLoggedInFlag(true);
        // set user role which is based on the logged username (email)
        this.specifyUserRole(user.email);
      } else {
        // user is unauthenticated, so we remove the loggedIn flag
        this.removeUserLoggedInFlag();
        // clear user (set null)
        this.clearUser();
        // remove JWT token
        this.removeJwtToken();
      }
    });
  }

  /**
   * Authenticate the user with the given username and password.
   */
  public loginWithUserAndPassword(email: string, password: string): Promise<boolean>
  {
    const p = this.afAuth.signInWithEmailAndPassword(email, password);
    return this.handleLoginProcess(p);
  }

  /**
   * Authenticate the user with Google account 
   */
  public loginWithGoogle(): Promise<boolean>
  {
    const p = this.afAuth.signInWithPopup(new firebase.auth.GoogleAuthProvider());
    return this.handleLoginProcess(p);
  }

  /**
   * Authenticate the user with Microsoft (Azure) account 
   */
  public loginWithMicrosoft(): Promise<boolean>
  {
    const provider = new firebase.auth.OAuthProvider('microsoft.com');
    provider.setCustomParameters({
      // Optional "tenant" parameter in case you are using an Azure AD tenant.
      // eg. '8eaef023-2b34-4da1-9baa-8bc8c9d6a490' or 'contoso.onmicrosoft.com'
      // or "common" for tenant-independent tokens.
      // The default value is "common".
      tenant: environment.AzureTenant
    });
    const p = this.afAuth.signInWithPopup(provider);
    return this.handleLoginProcess(p);
  }

  public loginWithGitHub(): Promise<boolean>
  {
    const provider = new firebase.auth.GithubAuthProvider();
    provider.setCustomParameters({
      'allow_signup': 'false'
    });
    const p = this.afAuth.signInWithPopup(provider);
    return this.handleLoginProcess(p);
  }

  public loginWithFacebook(): Promise<boolean>
  {
    const provider = new firebase.auth.FacebookAuthProvider();
    provider.setCustomParameters({
      'display': 'popup'
    });
    const p = this.afAuth.signInWithPopup(provider);
    return this.handleLoginProcess(p);
  }

  private handleLoginProcess(loginPromise: Promise<firebase.auth.UserCredential>): Promise<boolean>
  {
    return loginPromise
    .then(res => {
        console.log('Authentication is successful with ' + res.user?.email);
        // get the JWT token used to identify the user to a Firebase service
        res.user?.getIdToken().then(token =>{
          this.storeJwtToken(token);
        });
        this.setUser(res.user);
        this.setUserLoggedInFlag(true);
        // set user role which is based on the logged username (email)
        this.specifyUserRole(res.user?.email);
        return true;
    })
    .catch(err => {
        console.log('Error during the authentication: ', err?.message);
        this.removeUserLoggedInFlag();
        this.clearUser();
        this.removeJwtToken();
        return false;
    });
  }

  public logout(): Promise<boolean>
  {
    return this.afAuth
    .signOut()
    .then(() => {
        this.firebaseAuthStateSubscription.unsubscribe();
        this.removeUserLoggedInFlag();
        this.clearUser();
        this.removeJwtToken();
        return true;
    })
    .catch(err => {
      console.log('Error during the logout process: ', err?.message);
      this.firebaseAuthStateSubscription.unsubscribe();
      this.removeUserLoggedInFlag();
      this.clearUser();
      this.removeJwtToken();
      return false;
    });
  }

  // observable which emits boolean when status changed for loggedIn flag.
  public getUserLoggedIn(): Observable<boolean>
  {
    return this.userLoggedInObservable;
  }

  // read the flag from the local storage
  public readUserLoggedInFlag(): boolean
  {
    return localStorage.getItem(this.USER_LOGGED_IN) === 'true' ? true : false;
  }

  // set the flag in the local storage
  // and also update value in the subject
  private setUserLoggedInFlag(isLoggedIn: boolean): void
  {
    localStorage.setItem(this.USER_LOGGED_IN, isLoggedIn ? 'true' : 'false');
    this.userLoggedInSubject.next(isLoggedIn);
  }

  // remove the flag from the local storage
   // and also update value in the subject
  private removeUserLoggedInFlag(): void
  {
    localStorage.removeItem(this.USER_LOGGED_IN);
    this.userLoggedInSubject.next(false);
  }


  public getUser()
  {
    return this.userObservable;
  }

  private setUser(user: firebase.User | null)
  {
    this.userSubject.next(user);
  }

  private clearUser()
  {
    this.userSubject.next(null);
  }


  public getUserRole()
  {
    return this.userRoleObservable;
  }

  private specifyUserRole(userName: string | null | undefined)
  {
    if (userName === 'admin@mspt-demo.com') {
      // this is the administrator. set admin role.
      this.setUserRole(ROLE_ADMIN);
    }
    else {
      // this is normal user
      this.setUserRole(ROLE_USER);
    }
  }

  private setUserRole(role: string)
  {
    this.userRoleSubject.next(role);
  }


  public getJwtToken()
  {
    return localStorage.getItem(this.JWT_TOKEN);
  }

  private storeJwtToken(jwt: string)
  {
    localStorage.setItem(this.JWT_TOKEN, jwt);
  }

  private removeJwtToken()
  {
    localStorage.removeItem(this.JWT_TOKEN);
  }

}
