import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MsptFirebaseAuthService } from './firebase-auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private authenticationService: MsptFirebaseAuthService, private router: Router) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
    {
        console.log('Request URL: ' + request.url);

        return next.handle(request)
        .pipe(
            catchError(err => {
                if (err.status === 401) {
                    // auto logout if 401 response returned from api
                    this.authenticationService.logout();
                    //location.reload(true);
                    //location.reload();
                    // Navigate to the login page
                    this.router.navigate(['/auth']);
                }
                const error = err.error.message || err.statusText;
                return throwError(() => new Error(error));
            })
        )
    }
}
