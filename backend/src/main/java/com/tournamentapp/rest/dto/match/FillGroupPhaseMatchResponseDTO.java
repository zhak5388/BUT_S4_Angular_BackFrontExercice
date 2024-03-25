package com.tournamentapp.rest.dto.match;

import com.tournamentapp.rest.dto.match.GroupsPointsDTO.ParticipantGroupPointsDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
public class FillGroupPhaseMatchResponseDTO {
    public ParticipantGroupPointsDTO participant1Pts;
    public ParticipantGroupPointsDTO participant2Pts;
}
