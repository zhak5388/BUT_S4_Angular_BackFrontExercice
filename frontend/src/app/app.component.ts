import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TestComposantComponent} from "./test-composant/test-composant.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TestComposantComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
