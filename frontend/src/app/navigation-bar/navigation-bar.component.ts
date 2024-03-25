import {Component} from '@angular/core';
import {NgbCollapse, NgbNav, NgbNavItem, NgbNavLink} from "@ng-bootstrap/ng-bootstrap";
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-navigation-bar',
  standalone: true,
  imports: [
    NgbNav,
    NgbNavItem,
    NgbNavLink,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    NgbCollapse,
    NgIf
  ],
  templateUrl: './navigation-bar.component.html'
})
export class NavigationBarComponent {
  isNavBarCollapsed: boolean;
  isUserConnected: boolean;
  connectedUsername: string;

  constructor(private router: Router) {
    this.isNavBarCollapsed = true;
    const storedUsername = localStorage.getItem('connectedUsername');
    if (storedUsername) {
      this.isUserConnected = true;
      this.connectedUsername = storedUsername;
    } else {
      this.isUserConnected = false;
      this.connectedUsername = "";
    }
  }

  collapseNavBar() {
    this.isNavBarCollapsed = true;
  }

  manageCollapse() {
    this.isNavBarCollapsed = !this.isNavBarCollapsed;
  }

  login() {
    this.router.navigate(['/login']);
  }

  //La connection est simulée par l'ajout d'une valeur dans le local storage
  logout() {
    localStorage.removeItem("connectedUsername");
    this.isUserConnected = false;
  }
}
