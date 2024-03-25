package com.tournamentapp.repo;

import com.tournamentapp.domain.Participant;
import com.tournamentapp.domain.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParticipantRepo extends MongoRepository<Participant,String> {
    List<Participant> findAllByTournament(Tournament tournament);

}
