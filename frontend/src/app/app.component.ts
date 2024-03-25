import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {
  NgbCollapse,
  NgbDropdown,
  NgbDropdownItem,
  NgbDropdownMenu, NgbDropdownToggle,
  NgbNav,
  NgbNavItem,
  NgbNavLink, NgbNavLinkButton, NgbNavPane,
} from "@ng-bootstrap/ng-bootstrap";
import {NavigationBarComponent} from "./navigation-bar/navigation-bar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive,
    NgbNavItem, NgbNavLink, NgbNav,
    NgbDropdown, NgbDropdownMenu, NgbDropdownItem, NgbDropdownToggle, NgbNavPane, NgbNavLinkButton, NgbCollapse, NavigationBarComponent],
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'app-ng17';
}
