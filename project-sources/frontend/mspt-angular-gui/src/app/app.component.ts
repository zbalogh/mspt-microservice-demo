import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { MsptFirebaseAuthService } from './auth/firebase-auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  isLogged$: Observable<boolean>;

  userRole$: Observable<string>;

  /**
   * Constructor to inject dependencies
   */
  constructor(private readonly authService: MsptFirebaseAuthService)
  {
    this.isLogged$ = this.authService.getUserLoggedIn();
    this.userRole$ = this.authService.getUserRole();
  }

  public ngOnInit()
  {
  }

  /*
  public async login()
  {
    await this.authService.login();
  }

  public async logout()
  {
    await this.authService.logout();
  }
  */
 
}
