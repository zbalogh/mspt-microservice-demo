import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MsptFirebaseAuthService } from './firebase-auth.service';
import { getBaseWebURL } from 'src/app/app-constants';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private authenticationService: MsptFirebaseAuthService)
    {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
    {
        // check if user is logged in
        const isLoggedIn = this.authenticationService.readUserLoggedInFlag();

        // Read the JWT token from the authentication service
        // The JWT token is stored in the local storage after the successful login
        const token = this.authenticationService.getJwtToken();

        // if the absolute path is same as the location origin OR same as getBaseUrl
        // OR the relative path starts with /api
        // so then we allow appending Authorization header with JWT token
        const isSameOrigin = request.url.startsWith(location.origin) ||
                             request.url.startsWith(getBaseWebURL()) ||
                             request.url.startsWith('/api');

        // add "Authorization" header with JWT token if user is logged
        if (isSameOrigin && isLoggedIn && token) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }

        return next.handle(request);
    }

}
