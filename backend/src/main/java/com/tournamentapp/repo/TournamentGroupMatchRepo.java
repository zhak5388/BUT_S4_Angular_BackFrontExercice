package com.tournamentapp.repo;

import com.tournamentapp.domain.TournamentGroupMatch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentGroupMatchRepo extends MongoRepository<TournamentGroupMatch,String> {

}
