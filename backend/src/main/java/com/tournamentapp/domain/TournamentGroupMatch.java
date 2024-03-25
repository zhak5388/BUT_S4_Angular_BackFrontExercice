package com.tournamentapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
public class TournamentGroupMatch extends TournamentMatch {

    @DocumentReference
    public TournamentGroup group;

//    public int groupMatchIndex;


    public int goalAveragePoint1;
    public int goalAveragePoint2;

    public TournamentGroupMatch(
            Tournament tournament,
            TournamentGroup group,
            Participant participant1, Participant participant2
    ) {
        super(tournament, participant1, participant2);
        this.group = group;
    }

}
