//NOTE: A quoi ca sert?
import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {ApiModule, Configuration} from "./rest";
import {provideHttpClient} from "@angular/common/http";

const apiConfig: Configuration = new Configuration(
  {
    basePath: 'http://localhost:4200' // override default 'http://localhost:8080' for cors origin problems.. cf proxy
  });

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    importProvidersFrom(
      ApiModule.forRoot(() => apiConfig)
    )
  ]
};
