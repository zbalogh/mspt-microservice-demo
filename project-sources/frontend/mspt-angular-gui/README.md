# MsptAngularGui

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.2.6.

## Development server

Run `npm start` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

Run `npm run start-ssl` for a dev server with SSL support. Navigate to `https://localhost:4200/`. The application will automatically reload if you change any of the source files. The development dummy certificate and key files are in the "certs" folder: dummy-dev.crt, dummy-dev.key (If you want you can replace it with your own certificate.)

## Production Build

Run `npm run bp` to build the project in production mode. The build artifacts will be stored in the `dist/mspt-angular-gui` directory.

## Build and Push Docker image

Note: First build the project in production mode.

Run `docker rmi --force $(docker images -q 'zbalogh/mspt-angular-ui:latest' | uniq)` command to remove existing image from your locale docker image repository.

Run `docker build -t zbalogh/mspt-angular-ui:latest -f Dockerfile .`  command to build new docker image in your local docker image repository.

Run `docker push zbalogh/mspt-angular-ui:latest` command to push the docker image to the Docker Hub.

## If you have compiler issue in your VS Code Editor, you can solve it by choosing the right TypeScript version (use Workspace version)

https://stackoverflow.com/questions/52801814/this-syntax-requires-an-imported-helper-but-module-tslib-cannot-be-found-wit
