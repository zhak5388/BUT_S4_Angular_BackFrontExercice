package com.tournamentapp.rest.dto.tournament;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TournamentParticipantsDTO {
    public String tournamentId;

    public List<DetailedParticipantDTO> participants;

    @AllArgsConstructor
    public static class DetailedParticipantDTO {
        public ParticipantDTO participant;

        public LocalDateTime registeredDateTime;
        public String licenseCertificate;
    }

}
