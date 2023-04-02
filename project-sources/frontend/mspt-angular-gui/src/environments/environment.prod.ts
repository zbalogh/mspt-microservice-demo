import { firebaseConfig } from "./firebase";

export const environment = {
  production: true,
  authenticationProvider: 'firebase',
  // KeyCloak Configuration
  keycloakConfig : {
    url: 'https://keycloak-server:8182/keycloak',
    realm: 'mspt-microservice-realm',
    clientId: 'mspt-microservice-frontend',
    silentCheckSsoRedirectPage: '/assets/verify-sso.html'
  },
  // Firebase Configuration
  ...firebaseConfig
  ,
   // Azure Tenant(id or domain)
   AzureTenant: 'baloghzoltan1977outlook.onmicrosoft.com'
};
