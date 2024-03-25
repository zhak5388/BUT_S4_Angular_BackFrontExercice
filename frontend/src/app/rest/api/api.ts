export * from './tournamentRestController.service';
import { TournamentRestControllerService } from './tournamentRestController.service';
export * from './userRestController.service';
import { UserRestControllerService } from './userRestController.service';
export const APIS = [TournamentRestControllerService, UserRestControllerService];
