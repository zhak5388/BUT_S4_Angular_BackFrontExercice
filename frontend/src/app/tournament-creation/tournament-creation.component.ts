import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router, RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";

export interface TournamentRequest {
  name: string;
  registrationStartDate: string;
  registrationCloseDate: string;
  startDate: string;
  endDate: string;
  location: string;
  maxParticipants: number;
  nthPowerEliminationPhase: number;
}

@Component({
  selector: 'app-tournament-creation',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    RouterLink
  ],
  templateUrl: './tournament-creation.component.html'
})
export class TournamentCreationComponent {
  tournament: TournamentRequest;
  creationSuccess: boolean;
  wasSubmitted: boolean;

  constructor(private httpClient: HttpClient, private router: Router) {
    this.tournament = {
      "name": "Placeholder",
      "registrationStartDate": "2024-04-27",
      "registrationCloseDate": "2024-04-27",
      "startDate": "2024-04-27",
      "endDate": "2024-04-27",
      "location": "string",
      "maxParticipants": 50,
      "nthPowerEliminationPhase": 2
    }
    this.creationSuccess = false;
    this.wasSubmitted = false;
  }

  createTournament() {
    this.wasSubmitted = true;
    this.httpClient.post<TournamentRequest>('api/tournament', this.tournament)
      .subscribe(
        (response) => {
          console.log(response);
          this.creationSuccess = true;
        },
        (error) => {
          console.error('Une erreur est survenue lors du chargement:', error);
          this.creationSuccess = false;
        }
      );
  }
}
