import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RouterLink} from "@angular/router";
import {ActivatedRoute} from '@angular/router';
import {NgIf} from "@angular/common";
import {NgbNavLink} from "@ng-bootstrap/ng-bootstrap";

export interface TournamentResponse {
  id: string,
  name: string,
  state: string
}

@Component({
  selector: 'app-tournament-detail',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    NgbNavLink
  ],
  templateUrl: './tournament-detail.component.html'
})
export class TournamentDetailComponent {
  retrievedId: string | null;
  tournamentFound: boolean;
  tournament: TournamentResponse;

  constructor(private httpClient: HttpClient, private route: ActivatedRoute) {
    this.retrievedId = this.route.snapshot.paramMap.get('id');
    this.tournament = {id: "", name: "", state: ""};
    this.tournamentFound = false;
    this.getTournament();
  }

  getTournament() {
    this.httpClient.get<TournamentResponse>('api/tournament/' + this.retrievedId).subscribe(
      (response) => {
        this.tournament = response;
        this.tournamentFound = true;
      }, (error) => {
        console.error('Une erreur est survenue lors du chargement:', error);
      }
    )
  }
}
