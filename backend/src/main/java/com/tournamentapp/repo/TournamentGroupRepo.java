package com.tournamentapp.repo;

import com.tournamentapp.domain.Tournament;
import com.tournamentapp.domain.TournamentGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentGroupRepo extends MongoRepository<TournamentGroup,String> {
    // TournamentGroup findByTournamentName(Tournament tournament, String name);
}
