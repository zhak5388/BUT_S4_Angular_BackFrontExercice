package com.tournamentapp.rest.dto.match;

import com.tournamentapp.rest.dto.tournament.ParticipantDTO;
import com.tournamentapp.rest.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
public class GroupsPointsDTO {
    public String tournamentId;
    public String name;
    public List<GroupPointsDTO> groupPts;


    @NoArgsConstructor @AllArgsConstructor
    public static class GroupPointsDTO {
        public String groupName;
        public List<ParticipantGroupPointsDTO> participantPoints;
        public List<RemainingGroupMatchDTO> remainingGroupMatches;

    }

    @NoArgsConstructor @AllArgsConstructor
    public static class ParticipantGroupPointsDTO {
        public ParticipantDTO participant;
        // public ParticipantMatchPoints pts;
        public int points;
        public int goalAverageSets;
        public int goalAveragePoints;
        public int remainingMatchCount;

        public static final Comparator<ParticipantGroupPointsDTO> COMPARATOR =
                Comparator.<ParticipantGroupPointsDTO,Integer>comparing(x -> x.points)
                        .thenComparing(x -> x.goalAverageSets)
                        .thenComparing(x -> x.goalAveragePoints);

    }

    @NoArgsConstructor @AllArgsConstructor
    public static class RemainingGroupMatchDTO {
        public String id;
        public ParticipantDTO part1;
        public ParticipantDTO part2;
    }

    @NoArgsConstructor @AllArgsConstructor
    public static class RemainingMatchesParticipantGroupPointsDTO {
        public ParticipantGroupPointsDTO groupPts;
        public List<RemainingGroupMatchDTO> remainingMatches;
    }

}
