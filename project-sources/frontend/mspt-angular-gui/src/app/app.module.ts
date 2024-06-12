import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { environment } from '../environments/environment';

import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';

import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule } from '@angular/fire/compat/auth';

import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { MyOrdersComponent } from './pages/my-orders/my-orders.component';
import { ProductCatalogListComponent } from './pages/product-catalog-list/product-catalog-list.component';
import { ShoppingComponent } from './pages/shopping/shopping.component';
import { InventoryListComponent } from './pages/inventory-list/inventory-list.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AuthenticationComponent } from './auth/authentication/authentication.component';
import { NgbModule, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { AUTH_PROVIDER_KEYCLOAK, WEBAPP_BASE_HREF } from './app-constants';
import { JwtInterceptor } from './auth/jwt.interceptor';
import { ErrorInterceptor } from './auth/error.interceptor';
import { MyCartComponent } from './pages/my-cart/my-cart.component';
import { ToastsContainer } from './services/toasts-container.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ModalModule } from 'ngx-bootstrap/modal';


function initializeKeycloakFactory(keycloak: KeycloakService)
{
  // we return a function which does the keycloak initialization
  return () => {
      if (environment.authenticationProvider !== AUTH_PROVIDER_KEYCLOAK) {
        // it is not keycloak mode
        console.log('The authentication mode is not keycloak.');
        return;
      }
      keycloak.init({
        config: {
          url: environment.keycloakConfig.url,
          realm: environment.keycloakConfig.realm,
          clientId: environment.keycloakConfig.clientId
        },
        /*
        initOptions: {
          onLoad: 'login-required',
          checkLoginIframe: false
        }
        */
        initOptions: {
          onLoad: 'check-sso',
          checkLoginIframe: true,
          silentCheckSsoRedirectUri: window.location.origin + WEBAPP_BASE_HREF + environment.keycloakConfig.silentCheckSsoRedirectPage
        }
      });
  }
}


@NgModule({
  declarations: [
    AppComponent,
    ToastsContainer,
    HomeComponent,
    MyOrdersComponent,
    ProductCatalogListComponent,
    ShoppingComponent,
    InventoryListComponent,
    PageNotFoundComponent,
    AuthenticationComponent,
    MyCartComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    //
    // Keycloak Angular Module for keycloak authentication
    KeycloakAngularModule,
    //
    // Angular Firebase Module
    AngularFireModule.initializeApp(environment.firebaseConfig),
    //
    // Angular Firebase Auth Module for firebase authentication
    AngularFireAuthModule,
    NgbModule,
    NgbToastModule,
    ModalModule.forRoot(),
    FontAwesomeModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloakFactory,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
