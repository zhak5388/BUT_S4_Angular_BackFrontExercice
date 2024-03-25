import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-login',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './user-login.component.html'
})
export class UserLoginComponent {
  username: string;

  constructor(private router: Router) {
    this.username = '';
  }

  login() {
    localStorage.setItem("connectedUsername", this.username);
    this.router.navigate(['/']).then(() => {
      window.location.reload();
    });
  }
}
