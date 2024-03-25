package com.tournamentapp.rest.dto.match;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractFillMatchRequestDTO {

    public String matchId;

    public String part1Id; // redundant with matchId
    public String part2Id; // redundant with matchId

    public List<SetScoreDTO> setScores;

    public LocalDateTime matchStartTime;
    public LocalDateTime matchEndTime;

}
