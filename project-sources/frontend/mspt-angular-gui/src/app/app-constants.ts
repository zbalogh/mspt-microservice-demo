
// constants for authentication provider types
export const AUTH_PROVIDER_KEYCLOAK = 'keycloak';
export const AUTH_PROVIDER_FIREBASE = 'firebase';
export const AUTH_PROVIDER_AZURE = 'azure';


// web application base href (it is also configured in the angular.json and package.json files)
export const WEBAPP_BASE_HREF = '/mspt-web/';


// roles
export const ROLE_ADMIN = "ROLE_ADMIN";
export const ROLE_USER = "ROLE_USER";


export function getBaseWebURL(): string
{
  let baseURL='';
  const protocol = location.protocol;
  const port = location.port;
  const hostname = location.hostname;

  if (protocol.startsWith('https')) {
    baseURL = 'https://' + hostname + (port ? ':'+port : '');
  } else {
    baseURL = 'http://' + hostname + (port ? ':'+port : '');
  }

  return baseURL;
}

export function getLocationOrigin(): string
{
  return window.location.origin;
}

