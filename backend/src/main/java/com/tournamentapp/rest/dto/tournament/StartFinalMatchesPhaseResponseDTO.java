package com.tournamentapp.rest.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
public class StartFinalMatchesPhaseResponseDTO {
    public List<ParticipantDTO> finalParticipants;
}
