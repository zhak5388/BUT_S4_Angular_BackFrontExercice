package com.tournamentapp.rest.dto.tournament;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TournamentsSearchResponseDTO {
    public int number;
    public int size;
    public int totalPages;
    public List<TournamentDTO> items;

}
