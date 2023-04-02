import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class MsptKeyCloakAuthGuard extends KeycloakAuthGuard
{
    constructor(
        protected readonly ngRouter: Router,
        protected readonly keycloak: KeycloakService
    ) {
        super(ngRouter, keycloak);
    }
    
    async isAccessAllowed(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Promise<boolean | UrlTree>
    {
        if (!this.authenticated) {
            await this.keycloak.login({
                redirectUri: window.location.origin + state.url,
            });
        }

        return this.authenticated;
    }

}
