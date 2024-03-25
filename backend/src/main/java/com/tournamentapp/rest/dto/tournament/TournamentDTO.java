package com.tournamentapp.rest.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
public class TournamentDTO {
    public String id;
    public String name;
    public TournamentState state;

}
