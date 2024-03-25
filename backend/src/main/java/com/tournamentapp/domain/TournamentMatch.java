package com.tournamentapp.domain;

import com.tournamentapp.rest.dto.match.MatchPoints;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

public class TournamentMatch {

    @Id
    public String id;

    @DocumentReference
    public Tournament tournament;

    @DocumentReference
    public TournamentReferee referee;

    @DocumentReference
    public Participant participant1;

    @DocumentReference
    public Participant participant2;

    public LocalDateTime startTime;
    public LocalDateTime endTime;

    @NoArgsConstructor @AllArgsConstructor
    public static class SetScore {
        public int score1;
        public int score2;
    }

    public List<SetScore> setScores;

    // computed from setScores
    // public boolean winner1;
    public MatchPoints pts; // = MatchPoints.toMatchPoints(setScores);


    public TournamentMatch() {}

    public TournamentMatch(
            Tournament tournament,
            Participant participant1, Participant participant2
    ) {
        this.tournament = tournament;
        this.participant1 = participant1;
        this.participant2 = participant2;
    }

}
