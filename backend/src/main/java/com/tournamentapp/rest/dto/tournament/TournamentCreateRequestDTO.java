package com.tournamentapp.rest.dto.tournament;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TournamentCreateRequestDTO {
    public String name;

    public LocalDate registrationStartDate;
    public LocalDate registrationCloseDate;

    public LocalDate startDate;
    public LocalDate endDate;
    public String location;

    public int maxParticipants;
    public int nthPowerEliminationPhase;

}
