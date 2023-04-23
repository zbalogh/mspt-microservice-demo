import { firebaseConfig } from "./firebase";

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  authenticationProvider: 'firebase',
  // KeyCloak Configuration
  keycloakConfig : {
    url: 'https://keycloak-server:8182/keycloak',
    realm: 'mspt-microservice-realm',
    clientId: 'mspt-microservice-frontend',
    silentCheckSsoRedirectPage: '/assets/verify-sso.html'
  },
  
  // Firebase Configuration
  ...firebaseConfig,

  // Azure Tenant(id or domain)
  AzureTenant: 'baloghzoltan1977outlook.onmicrosoft.com'
};


/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
