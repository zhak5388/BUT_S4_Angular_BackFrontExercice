package com.tournamentapp.rest;

import com.tournamentapp.rest.dto.match.*;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.ParticipantGroupPointsDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.RemainingGroupMatchDTO;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.RemainingMatchesParticipantGroupPointsDTO;
import com.tournamentapp.rest.dto.tournament.*;
import com.tournamentapp.service.TournamentService;
import com.tournamentapp.utils.LsUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tournament", produces = "application/json")
@OpenAPIDefinition(
        // tags = { Tag("Tournament") }
)
public class TournamentRestController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping() // path="/new"
    @Operation(description = "create a new Tournament")
    public TournamentCreateResponseDTO create(
            @RequestBody TournamentCreateRequestDTO req) {
        return tournamentService.create(req);
    }

    @GetMapping()
    @Operation(description = "List Tournaments")
    public List<TournamentDTO> list(
            @RequestParam(name="maxResults", defaultValue = "50") int maxResults
    ) {
        val req = new TournamentsSearchRequestDTO();
        req.maxResults = maxResults;
        // TOADD more default criteria
        val tmp = tournamentService.searchTournaments(req);
        return tmp.items;
    }

    @PostMapping("/search")
    @Operation(description = "Search Tournaments")
    public TournamentsSearchResponseDTO searchTournaments(
            @RequestBody TournamentsSearchRequestDTO req) {
        return tournamentService.searchTournaments(req);
    }

    @GetMapping(path="/{tournamentId}")
    @Operation(description = "get Tournament detail")
    public TournamentDTO getById(
            @PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getDTO(tournamentId);
    }

    @PostMapping(path="/{tournamentId}/participants")
    @Operation(description = "add a participant to a Tournament")
    public AddParticipantResponseDTO create(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody AddParticipantRequestDTO req) {
        return tournamentService.addParticipant(tournamentId, req);
    }

    @PostMapping(path="/{tournamentId}/close-participations")
    @Operation(description = "close registrations for participating to Tournament")
    public void closeParticipations(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody CloseParticipationsRequestDTO req
    ) {
        tournamentService.closeParticipations(tournamentId, req);
    }

    @PostMapping(path="/{tournamentId}/start-groups-phase")
    @Operation(description = "generate the random groups of participants, start the Group phase matches")
    public StartGroupPhaseResponseDTO startGroupsPhase(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody StartGroupsPhaseRequestDTO req) {
        return tournamentService.startGroupsPhase(tournamentId, req);
    }

    @PostMapping(path="/{tournamentId}/fill-group-match-score")
    @Operation(description = "Fill a group match score (only by the Referee of the match)")
    public FillGroupPhaseMatchResponseDTO fillGroupPhaseMatchScore(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody FillGroupPhaseMatchRequestDTO req) {
        return tournamentService.fillGroupPhaseMatchScore(tournamentId, req);
    }

    @GetMapping(path="/{tournamentId}/group-points")
    @Operation(description = "compute participant points per groups")
    public GroupsPointsDTO getGroupsPoints(
            @PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getGroupPoints(tournamentId);
    }

    @GetMapping(path="/{tournamentId}/group-points/{participantId}")
    @Operation(description = "compute participant points in group and remaining matches")
    public RemainingMatchesParticipantGroupPointsDTO getParticipantGroupsPoints(
            @PathVariable("tournamentId") String tournamentId,
            @PathVariable("participantId") String participantId) {
        val tmp = tournamentService.getGroupPoints(tournamentId);
        ParticipantGroupPointsDTO groupPts = null;
        List<RemainingGroupMatchDTO> remainingMatches = null;
        for(val groupPt: tmp.groupPts) {
            val found = LsUtils.findFirst(groupPt.participantPoints, x -> x.participant.id.equals(participantId));
            if (found != null) {
                groupPts = found;
                remainingMatches = LsUtils.filter(groupPt.remainingGroupMatches,
                        x -> x.part1.id.equals(participantId) || x.part2.id.equals(participantId));
                break;
            }
        }
        return new RemainingMatchesParticipantGroupPointsDTO(groupPts, remainingMatches);
    }


    @PostMapping(path="/{tournamentId}/start-final-match-phase")
    @Operation(description = "start final phase matches: peek first of each groups, random shuffle in grid, setup elimination matches")
    public StartFinalMatchesPhaseResponseDTO startFinalMatchesPhase(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody StartFinalMatchesPhaseRequestDTO req) {
        return tournamentService.startFinalMatchesPhase(tournamentId, req);
    }

    @PostMapping(path="/{tournamentId}/final-match-phase-scores")
    @Operation(description = "Fill a final phase match score (only by the Referee of the match)")
    public FillFinalPhaseMatchResponseDTO fillFinalPhaseMatchScore(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody FillFinalPhaseMatchRequestDTO req) {
        return tournamentService.fillFinalPhaseMatchScore(tournamentId, req);
    }

    @GetMapping(path="/{tournamentId}/final-match-phase-scores")
    @Operation(description = "Get final phase matches score")
    public FinalPhaseMatchScoresDTO getFinalPhaseMatchScores(
            @PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getFinalPhaseMatchScore(tournamentId);
    }

}
