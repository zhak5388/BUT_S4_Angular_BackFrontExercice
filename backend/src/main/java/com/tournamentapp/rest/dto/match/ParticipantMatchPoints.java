package com.tournamentapp.rest.dto.match;

import lombok.val;

import java.util.Collection;

public record ParticipantMatchPoints(
        int points,
        int goalAverageSets,
        int goalAveragePoints
) {
}
