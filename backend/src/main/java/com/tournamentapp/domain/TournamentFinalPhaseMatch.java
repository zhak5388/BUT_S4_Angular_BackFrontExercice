package com.tournamentapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TournamentFinalPhaseMatch extends TournamentMatch {

    public int nthPowerPhase;
    public int matchGridRowIndex;

    public TournamentFinalPhaseMatch() {}

    public TournamentFinalPhaseMatch(Tournament tournament, int nthPowerPhase, int matchGridRowIndex) {
        super.tournament = tournament;
        this.nthPowerPhase = nthPowerPhase;
        this.matchGridRowIndex = matchGridRowIndex;
    }

}
