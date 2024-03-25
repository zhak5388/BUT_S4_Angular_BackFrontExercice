package com.tournamentapp.domain;

import com.tournamentapp.rest.dto.tournament.TournamentState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Tournament {

    @Id
    public String id;

    public String name;

    public TournamentState state;

    public int maxParticipants;
    public int nthPowerFinalPhase;
    public LocalDate startDate;
    public LocalDate endDate;
    public String location;


    public int finalParticipantCount() {
        return pow2(nthPowerFinalPhase);
    }
    public static int pow2(int pow) {
        int res = 1; // do not use Math.pow().. rounding double?
        for (int i = 1; i < pow; i++) {
            res *= 2;
        }
        return res;
    }

    public LocalDateTime createdDateTime;

    public LocalDate registrationStartDate;
    public LocalDate registrationCloseDate;

    public LocalDateTime groupMatchStartDate;
    public LocalDateTime finalPhaseMatchStartDateTime;
    public LocalDateTime endDateTime;

//    @DocumentReference
//    public List<Participant> participants;

    @DocumentReference
    public List<TournamentGroup> groups;

    @DocumentReference
    public List<Participant> finalPhaseParticipantGrid;

    @DocumentReference
    public List<TournamentFinalPhaseMatch> finalPhaseMatches;



    public void ensureState(TournamentState expectedState) {
        if (state != expectedState) {
            throw new IllegalArgumentException("expected tournament#" + id + " state " + expectedState + ", was " + state);
        }
    }

}
