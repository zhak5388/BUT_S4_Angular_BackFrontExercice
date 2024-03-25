package com.tournamentapp.repo;

import com.tournamentapp.domain.TournamentFinalPhaseMatch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentFinalPhaseMatchRepo extends MongoRepository<TournamentFinalPhaseMatch,String> {

}
