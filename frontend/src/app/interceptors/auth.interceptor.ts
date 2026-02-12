import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Auth Interceptor
 * Automatically adds Authorization header to all HTTP requests
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {

  // Get auth token from localStorage
  const authToken = localStorage.getItem('authToken');

  // If token exists, clone request and add Authorization header
  if (authToken) {
    req = req.clone({
      setHeaders: {
        Authorization: `Basic ${authToken}`
      }
    });
  }

  // Pass the request to the next handler
  return next(req);
};
