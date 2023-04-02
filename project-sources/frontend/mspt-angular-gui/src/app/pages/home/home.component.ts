import { Component, OnInit } from '@angular/core';
import firebase from 'firebase/compat/app';
import { Observable } from 'rxjs';
import { MsptFirebaseAuthService } from 'src/app/auth/firebase-auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  isLogged$: Observable<boolean>;
  user$: Observable<firebase.User | null>;

  isUserLogged: boolean = false;
  initialized: boolean = false;
  
  //user: firebase.User | null = null;
  //userProfile: KeycloakProfile | null = null;
  //userRoles: string[] = [];

   /**
   * Constructor to inject dependencies
   */
  constructor(private readonly authService: MsptFirebaseAuthService)
  {
    this.isLogged$ = this.authService.getUserLoggedIn();
    this.user$ = this.authService.getUser();
  }

  public async ngOnInit()
  {
    // subscribe for user loggedIn
    this.isLogged$.subscribe(logged =>
    {
        // received the loggedIn flag
        this.isUserLogged = logged;
        this.initialized = true;
    });
  }

}
