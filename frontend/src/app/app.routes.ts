import {Routes} from '@angular/router';
import {TournamentCreationComponent} from "./tournament-creation/tournament-creation.component";
import {UserLoginComponent} from "./user-login/user-login.component";
import {TournamentListComponent} from "./tournament-list/tournament-list.component";
import {TournamentDetailComponent} from "./tournament-detail/tournament-detail.component";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {HomePageComponent} from "./home-page/home-page.component";

export const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'tournament/new', component: TournamentCreationComponent},
  {path: 'login', component: UserLoginComponent},
  {path: 'tournaments', component: TournamentListComponent},
  {path: 'tournaments/:id', component: TournamentDetailComponent},
  {path: '**', component: PageNotFoundComponent}
];
