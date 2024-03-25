package com.tournamentapp.service;

import com.tournamentapp.domain.*;
import com.tournamentapp.rest.dto.match.FinalPhaseMatchScoresDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.GroupPointsDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.ParticipantGroupPointsDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.RemainingGroupMatchDTO;
import com.tournamentapp.rest.dto.match.ParticipantMatchPoints;
import com.tournamentapp.rest.dto.tournament.ParticipantDTO;
import com.tournamentapp.rest.dto.tournament.TournamentDTO;
import com.tournamentapp.rest.dto.tournament.TournamentParticipantsDTO;
import com.tournamentapp.rest.dto.tournament.TournamentParticipantsDTO.DetailedParticipantDTO;
import com.tournamentapp.rest.dto.tournament.TournamentsSearchResponseDTO;
import com.tournamentapp.rest.dto.user.UserDTO;
import com.tournamentapp.utils.LsUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class DTOMapper {

    public TournamentsSearchResponseDTO toTournamentsSearchResponseDTO(Page<Tournament> src) {
        return new TournamentsSearchResponseDTO(
                src.getNumber(), src.getSize(), src.getTotalPages(),
                // src.getTotalElements()
                LsUtils.map(src.toList(), x -> toTournamentDTO(x)));
    }

    public TournamentDTO toTournamentDTO(Tournament src) {
        return new TournamentDTO(src.id, src.name, src.state);
    }

    public ParticipantDTO toParticipantDTO(Participant src) {
        return new ParticipantDTO(src.id, toUserDTO(src.user));
    }

    public TournamentParticipantsDTO toTournamentParticipantsDTO(String tournamentId, List<Participant> participants) {
        List<DetailedParticipantDTO> detailedParticipants = LsUtils.map(
                participants, p -> toDetailedParticipantDTO(p));
        return new TournamentParticipantsDTO(tournamentId, detailedParticipants);
    }

    public DetailedParticipantDTO toDetailedParticipantDTO(Participant src) {
        return new DetailedParticipantDTO(toParticipantDTO(src),
                src.registeredDateTime, src.licenseCertificate);
    }

    public UserDTO toUserDTO(User src) {
        return new UserDTO(src.id, src.firstName, src.lastName);
    }


    public GroupsPointsDTO toGroupsPointsDTO(Tournament tournament, List<TournamentGroup> groups) {
        return new GroupsPointsDTO(tournament.id, tournament.name, LsUtils.map(groups, x -> toGroupPointsDTO(x)));
    }


    @RequiredArgsConstructor
    protected static class ParticipantGroupPointsDTOBuilder {
        public final Participant participant;
        int matchCount;
        int points;
        int goalAverageSets;
        int goalAveragePoints;

        public static ParticipantGroupPointsDTOBuilder getOrCreate(
                Map<String, ParticipantGroupPointsDTOBuilder> map,
                Participant part) {
            return map.computeIfAbsent(part.id, x -> new ParticipantGroupPointsDTOBuilder(part));
        }

        public void addMatchPoints(ParticipantMatchPoints pts) {
            this.matchCount++;
            this.points += pts.points();
            this.goalAverageSets += pts.goalAverageSets();
            this.goalAveragePoints += pts.goalAveragePoints();
        }

        protected ParticipantGroupPointsDTO toParticipantGroupPointsDTO(
                Function<Participant, ParticipantDTO> partToDTOFunc,
                int expectedMatchCount
        ) {
            val participantDTO = partToDTOFunc.apply(participant);
            int remainingMatchCount = expectedMatchCount - matchCount;
            return new ParticipantGroupPointsDTO(participantDTO,
                points, goalAverageSets, goalAveragePoints, remainingMatchCount);
        }

    }

    public GroupPointsDTO toGroupPointsDTO(TournamentGroup src) {
        val partPtsById = new HashMap<String, ParticipantGroupPointsDTOBuilder>();
        for(val part: src.participants) {
            ParticipantGroupPointsDTOBuilder.getOrCreate(partPtsById, part);
        }
        val remainingGroupMatches = new ArrayList<RemainingGroupMatchDTO>();
        for(val match : src.groupMatches) {
            if (match.pts != null) {
                ParticipantGroupPointsDTOBuilder.getOrCreate(partPtsById, match.participant1)
                        .addMatchPoints(match.pts.part1Pts());
                ParticipantGroupPointsDTOBuilder.getOrCreate(partPtsById, match.participant2)
                        .addMatchPoints(match.pts.part2Pts());
            } else {
                remainingGroupMatches.add(toRemainingGroupMatchDTO(match));
            }
        }
        val expectedMatchCount = src.participants.size() - 1;
        val partPts = LsUtils.map(partPtsById.values(),
                x -> x.toParticipantGroupPointsDTO(part -> toParticipantDTO(part), expectedMatchCount));
        // sort by points, descending
        Collections.sort(partPts, ParticipantGroupPointsDTO.COMPARATOR);
        return new GroupPointsDTO(src.name, partPts, remainingGroupMatches);
    }

    private RemainingGroupMatchDTO toRemainingGroupMatchDTO(TournamentGroupMatch src) {
        return new RemainingGroupMatchDTO(src.id,
                toParticipantDTO(src.participant1), toParticipantDTO(src.participant2));
    }

    public FinalPhaseMatchScoresDTO getFinalPhaseMatchScoresDTO(Tournament tournament) {
        return new FinalPhaseMatchScoresDTO(
                // TODO
        );
    }

}
