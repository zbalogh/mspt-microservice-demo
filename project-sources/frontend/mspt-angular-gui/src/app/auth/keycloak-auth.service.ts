import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
    providedIn: 'root'
  })
  export class MsptKeyCloakAuthService {

    /**
     * Constructor
     */
    constructor(private readonly keycloak: KeycloakService)
    {
    }

    public async loadUserProfile()
    {
      return await this.keycloak.loadUserProfile();
    }

    public getUserRoles()
    {
      return this.keycloak.getUserRoles(true);
    }

    public async login()
  {
    return await this.keycloak.login();
  }

    public async logout()
    {
      return await this.keycloak.logout(window.location.origin + '/home');
    }

    public async isUserLoggedIn()
    {
      const isLogged = await this.keycloak.isLoggedIn();
      console.log('Keycloak isLogged=' + isLogged);
      return isLogged;
    }

    public async getJwtToken()
    {
      return await this.keycloak.getToken();
    }

  }
