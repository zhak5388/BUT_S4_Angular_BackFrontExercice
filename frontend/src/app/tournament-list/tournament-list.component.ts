import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";

export interface TournamentResponse {
  id: string,
  name: string,
  state: string
}

@Component({
  selector: 'app-tournament-list',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink
  ],
  templateUrl: './tournament-list.component.html'
})
export class TournamentListComponent {
  tournaments: TournamentResponse[] = [];

  constructor(private httpClient: HttpClient) {
    this.getTournaments();
  }

  getTournaments() {
    this.httpClient.get<TournamentResponse[]>('api/tournament')
      .subscribe(
        (response) => {
          this.tournaments = response;
          //Pour obtenir les 5 plus récents (En attendant implémentation pagination)
          this.tournaments = this.tournaments.slice(-5).reverse();
        },
        (error) => {
          console.error('Une erreur est survenue lors du chargement:', error);
        }
      );
  }
}
