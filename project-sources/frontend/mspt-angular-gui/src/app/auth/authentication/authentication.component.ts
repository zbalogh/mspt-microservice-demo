import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { WEBAPP_BASE_HREF } from 'src/app/app-constants';
import { MsptFirebaseAuthService } from '../firebase-auth.service';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent implements OnInit {

  // fields that are used in the template
  message = '';
  username = '';
  password = '';

  logoutText = 'In order to sign out, please click on the Logout button.';
  loginText = 'In order to sign in, please click on the Login button.';
  authInProgress = 'Authentication in progress...';
  authFailed = 'Invalid username or password!';

  isLogged$: Observable<boolean>;
  isUserLogged: boolean = false;

  constructor(public authService: MsptFirebaseAuthService,
              public router: Router)
  {
    this.isLogged$ = this.authService.getUserLoggedIn();
  }

  ngOnInit(): void {
    this.isLogged$.subscribe(logged =>
      {
          // received loggedIn flag
          this.isUserLogged = logged;
          // and now we initialize the message used on the GUI
          this.initMessage();
      });
  }

  initMessage()
  {
    // based on the loggedIn flag we initialize the message
    if (this.isLoggedin()) {
        this.message =  this.logoutText;
    } else {
        this.message =  this.loginText;
    }
  }

  setMessage(message: string)
  {
    this.message = message;
  }

  loginWithUserAndPassword()
  {
    this.setMessage(this.authInProgress);
    const p = this.authService.loginWithUserAndPassword(this.username, this.password);
    this.handleLoginProcess(p);
  }

  loginWithGoogle()
  {
    this.setMessage(this.authInProgress);
    const p = this.authService.loginWithGoogle();
    this.handleLoginProcess(p);
  }

  loginWithMicrosoft()
  {
    this.setMessage(this.authInProgress);
    const p = this.authService.loginWithMicrosoft();
    this.handleLoginProcess(p);
  }

  loginWithGitHub()
  {
    this.setMessage(this.authInProgress);
    const p = this.authService.loginWithGitHub();
    this.handleLoginProcess(p);
  }

  loginWithFacebook()
  {
    this.setMessage(this.authInProgress);
    const p = this.authService.loginWithFacebook();
    this.handleLoginProcess(p);
  }

  private handleLoginProcess(loginPromise: Promise<boolean>)
  {
    loginPromise
    .then(() => {
      // clear the message
      this.setMessage('');

      // if the authentication is success then redirect the requested/attempted URL
      if (this.isLoggedin()) {
        // Get the redirect URL from our auth service
        // If no redirect has been set, use the default
        //const redirect = this.authService.redirectUrl ? this.router.parseUrl(this.authService.redirectUrl) : '/';
        //this.router.navigateByUrl(redirect);
        //
        // after successful login, we redirect to the web base (href) URL
        // it forces reinitializing the whole application
        window.location.href = window.location.origin + WEBAPP_BASE_HREF;
        //window.location.reload();
      } else {
        // the authentication is failed, show error message
        this.setMessage(this.authFailed);
      }
    });
  }

  logout() {
    this.authService.logout()
    .then(() => {
        this.initMessage();
        // after the successful logout, we just reload the browser to force reinitializing the whole application
        window.location.reload();
    });
  }

  isLoggedin()
  {
    return this.isUserLogged;
  }

}
