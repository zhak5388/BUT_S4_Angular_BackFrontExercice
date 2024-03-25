package com.tournamentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document
public class TournamentGroup {

    @Id
    public String id;

    public String name;

    @DocumentReference
    public List<Participant> participants;

    @DocumentReference
    public List<TournamentGroupMatch> groupMatches;

}
