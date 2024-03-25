package com.tournamentapp.rest.dto.tournament;

import com.tournamentapp.rest.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
public class ParticipantDTO {
    public String id;
    public UserDTO user;
    // public UserDTO user2; // in case of double team

}
