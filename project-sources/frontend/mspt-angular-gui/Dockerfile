FROM nginx:alpine

COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

COPY docker/health.html /usr/share/nginx/html/health.html

COPY docker/start.html /usr/share/nginx/html/index.html

COPY dist/mspt-angular-gui /usr/share/nginx/html/mspt-web

EXPOSE 8050
