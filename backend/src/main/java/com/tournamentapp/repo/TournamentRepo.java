package com.tournamentapp.repo;

import com.tournamentapp.domain.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentRepo extends MongoRepository<Tournament,String> {
    Tournament findByName(String name);

}
