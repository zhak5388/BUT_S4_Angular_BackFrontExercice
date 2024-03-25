import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-test-composant',
  standalone: true,
  imports: [],
  templateUrl: './test-composant.component.html',
})
export class TestComposantComponent {

  // Don't forget to import provideHttpClient() in app.config.ts
  constructor(private httpClient : HttpClient ) {
  }
  onSubmit(){
    //Don't forget proxy.conf.json
    this.httpClient.post("/api/player/add", {name: "jean"}).subscribe(
      data => {console.log(data)},
      error => {
        console.log("There is an error: " + error)
      }
    )
  }

}
