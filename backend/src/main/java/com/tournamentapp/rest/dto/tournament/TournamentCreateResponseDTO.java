package com.tournamentapp.rest.dto.tournament;

import java.time.LocalDateTime;

public class TournamentCreateResponseDTO {

    public TournamentDTO tournament;
    public LocalDateTime createdTimestamp;
    public String createdByUser;
    public LocalDateTime lastUpdatedTimestamp;
    public String lastUpdatedByUser;

}
