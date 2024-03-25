package com.tournamentapp.service;

import com.tournamentapp.domain.*;
import com.tournamentapp.domain.TournamentMatch.SetScore;
import com.tournamentapp.repo.*;
import com.tournamentapp.rest.dto.match.*;
import com.tournamentapp.rest.dto.match.GroupsPointsDTO.ParticipantGroupPointsDTO;
import com.tournamentapp.rest.dto.tournament.*;
import com.tournamentapp.utils.LsUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
@Service
public class TournamentService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TournamentRepo tournamentRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private TournamentGroupRepo tournamentGroupRepo;

    @Autowired
    private TournamentGroupMatchRepo tournamentGroupMatchRepo;

    @Autowired
    private TournamentFinalPhaseMatchRepo tournamentFinalPhaseMatchRepo;


    @Autowired
    private DTOMapper dtoMapper;

    //---------------------------------------------------------------------------------------------

    public TournamentCreateResponseDTO create(
            TournamentCreateRequestDTO req) {
        // step 1/3: unmarshal and check input
        String name = Objects.requireNonNull(req.name);
        if (name.length() <= 3) {
            throw new IllegalArgumentException("name too short");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("name too long");
        }

        // step 2/3 : business applicative code
        Tournament tournament = new Tournament();
        tournament.name = name;
        tournament.state = TournamentState.OpenForRegistration;
        tournament.registrationStartDate = req.registrationStartDate;
        tournament.registrationCloseDate = req.registrationCloseDate;
        tournament.startDate = req.startDate;
        tournament.endDate = req.endDate;
        tournament.location = req.location;
        tournament.createdDateTime = LocalDateTime.now();

        tournament = tournamentRepo.save(tournament);

        // step 3/3 :
        TournamentCreateResponseDTO res = new TournamentCreateResponseDTO();
        res.tournament = dtoMapper.toTournamentDTO(tournament);
        res.createdTimestamp = LocalDateTime.now();
        res.createdByUser = "<<currentUser (security not impl yet)";
        res.lastUpdatedTimestamp = LocalDateTime.now();
        res.lastUpdatedByUser = "<<currentUser (security not impl yet)";
        return res;
    }

    protected Tournament getById(String tournamentId) {
        Tournament tournament = tournamentRepo.findById(tournamentId).orElseThrow();
        return tournament;
    }

    public TournamentsSearchResponseDTO searchTournaments(TournamentsSearchRequestDTO req) {
        Pageable pageable = Pageable.ofSize((req.maxResults > 0)? req.maxResults : 50);
        val page = tournamentRepo.findAll(pageable);
        return dtoMapper.toTournamentsSearchResponseDTO(page);
    }

    public AddParticipantResponseDTO addParticipant(
            String tournamentId,
            AddParticipantRequestDTO req) {
        Tournament tournament = getById(tournamentId);
        tournament.ensureState(TournamentState.OpenForRegistration);
        User user = userRepo.findById(req.userId).orElseThrow();

        Participant part = new Participant();
        part.user = user;
        part.tournament = tournament;
        part = participantRepo.save(part);

        AddParticipantResponseDTO res = new AddParticipantResponseDTO();
        res.participantId = part.id;
        return res;
    }

    public TournamentDTO getDTO(String id) {
        Tournament tournament = getById(id);
        return dtoMapper.toTournamentDTO(tournament);
    }

    public void closeParticipations(
            String tournamentId,
            CloseParticipationsRequestDTO req) {
        Tournament tournament = getById(tournamentId);
        tournament.ensureState(TournamentState.OpenForRegistration);
        tournament.state = TournamentState.RegistrationClose;
        // tournament.closeRegistrationComment = req.comment;
        // tournament.registrationCloseDateTime = LocalDateTime.now();
        tournamentRepo.save(tournament);
    }

    public TournamentParticipantsDTO tournamentParticipants(String tournamentId) {
        Tournament tournament = getById(tournamentId);
        List<Participant> participants = participantRepo.findAllByTournament(tournament);
        return dtoMapper.toTournamentParticipantsDTO(tournamentId, participants);
    }



    @RequiredArgsConstructor
    private static class GroupBuilder {
        final int groupIdx;
        final String groupName; // "A", "B", ..
        final List<Participant> participants = new ArrayList<>(5);
        public TournamentGroup group;
    }

    private record ParticipantToGroupBuilder(
            Participant participant, GroupBuilder groupBuilder) {
    }

    public StartGroupPhaseResponseDTO startGroupsPhase(
            String tournamentId,
            StartGroupsPhaseRequestDTO req) {
        Tournament tournament = getById(tournamentId);
        tournament.ensureState(TournamentState.RegistrationClose);
        tournament.state = TournamentState.GroupMatchPhase;

        List<Participant> participants = participantRepo.findAllByTournament(tournament);
        int participantCount = participants.size();
        int groupCount = req.groupCount;
        if (participantCount < groupCount * 3) {
            throw new IllegalArgumentException("invalid number of group, or not enough participants, "
                    + "there should be at least 3 participants per group,"
                    + " got " + participantCount + " participants and " + groupCount + " groups");
        }
        // randomly shuffle participants
        val shuffledParticipants = new ArrayList<Participant>(participants);
        Collections.shuffle(shuffledParticipants);
        // create group lists
        GroupBuilder[] groupBuilders = new GroupBuilder[groupCount];
        for(int i = 0; i < groupCount; i++) {
            char ch = (char) ('A' + (i-0));
            groupBuilders[i]= new GroupBuilder(i, "" + ch);
        }
        // then round-robin add to groups
        val participantToGroupBuilder = new HashMap<String,ParticipantToGroupBuilder>();
        int groupIdx = 0;
        for(val p : shuffledParticipants) {
            val gb = groupBuilders[groupIdx];
            gb.participants.add(p);
            participantToGroupBuilder.put(p.id, new ParticipantToGroupBuilder(p, gb));
            groupIdx = (groupIdx+1) % groupCount;
        }
        // create groups + matches for group
        for(val gb : groupBuilders) {
            val groupParticipants = gb.participants;
            int groupParticipantCount = groupParticipants.size();
            if (groupParticipantCount < 3) {
                // should not occur
            }

            TournamentGroup group = new TournamentGroup();
            group.name = gb.groupName;
            group.participants = groupParticipants;
            group = tournamentGroupRepo.save(group);
            gb.group = group;

            val groupMatches = group.groupMatches = new ArrayList<>(groupParticipantCount * (groupParticipantCount-1) / 2);

            for(int part1Idx = 0; part1Idx < groupParticipantCount; part1Idx++) {
                val part1 = groupParticipants.get(part1Idx);
                for(int part2Idx = part1Idx+1; part2Idx < groupParticipantCount; part2Idx++) {
                    val part2 = groupParticipants.get(part2Idx);
                    // match part1 vs part2
                    TournamentGroupMatch match =
                            new TournamentGroupMatch(tournament, group, part1, part2);

                    match = tournamentGroupMatchRepo.save(match); // TOCHECK externalized/embedded document?
                    groupMatches.add(match);
                }
            }

            // update group (add matches)
            gb.group = tournamentGroupRepo.save(group);
        } // end for group

        // update participants in db (set group)
        for(val p : participants) {
            val partToGroupBuilder = participantToGroupBuilder.get(p.id);
            p.tournamentGroup = partToGroupBuilder.groupBuilder.group;
            participantRepo.save(p);
        }

        // update tournament in db (add groups)
        tournament.groups = LsUtils.map(groupBuilders, x -> x.group);
        tournamentRepo.save(tournament);

        val res = new StartGroupPhaseResponseDTO();
        // TOADD?
        return res;
    }

    public FillGroupPhaseMatchResponseDTO fillGroupPhaseMatchScore(
            String tournamentId,
            FillGroupPhaseMatchRequestDTO req) {
        val tournament = getById(tournamentId);
        val group = Objects.requireNonNull(
                LsUtils.findFirst(tournament.groups, x -> x.name.equals(req.group)));
        val match = Objects.requireNonNull(
                LsUtils.findFirst(group.groupMatches,
                    x -> x.id.equals(req.matchId)
                        && x.participant1.id.equals(req.part1Id) && x.participant2.id.equals(req.part2Id) // redundant with matchId
                ));
        doFillMatchScore(match, req.setScores, req.matchEndTime);

        // update db
        tournamentGroupMatchRepo.save(match); // TOCHECK externalized/embedded document?
        tournamentGroupRepo.save(group);
        // tournamentRepo.save(tournament);

        val resGroupPts = dtoMapper.toGroupPointsDTO(group);
        val part1Pts = LsUtils.findFirst(resGroupPts.participantPoints, x -> x.participant.id.equals(match.participant1.id));
        val part2Pts = LsUtils.findFirst(resGroupPts.participantPoints, x -> x.participant.id.equals(match.participant2.id));
        val res = new FillGroupPhaseMatchResponseDTO(part1Pts, part2Pts);
        return res;
    }

    protected void doFillMatchScore(TournamentMatch match,
                                    List<SetScoreDTO> setScores, LocalDateTime matchEndTime) {
        // TOADD ensure current user is the referee of the match, or admin
        if (match.setScores != null) throw new IllegalArgumentException("match score already filled");
        match.setScores = LsUtils.map(setScores, x -> new SetScore(x.score1, x.score2));
        match.pts = MatchPoints.toMatchPoints(setScores);
        match.endTime = matchEndTime;
    }

    public GroupsPointsDTO getGroupPoints(String tournamentId) {
        val tournament = getById(tournamentId);
        val groups = tournament.groups;
        return dtoMapper.toGroupsPointsDTO(tournament, groups);

    }

    public StartFinalMatchesPhaseResponseDTO startFinalMatchesPhase(
            String tournamentId,
            StartFinalMatchesPhaseRequestDTO req) {
        val tournament = getById(tournamentId);
        tournament.ensureState(TournamentState.GroupMatchPhase);
        tournament.state = TournamentState.FinalMatchPhase;
        tournament.finalPhaseMatchStartDateTime = LocalDateTime.now();

        // compute pts in each group
        GroupsPointsDTO groupsPoints = dtoMapper.toGroupsPointsDTO(tournament, tournament.groups);

        // extract first of each groups
        List<ParticipantDTO> selectedParticipants = new ArrayList<ParticipantDTO>();
        List<ParticipantGroupPointsDTO> remainCandidateParticipantPts = new ArrayList<>();
        for(val groupPts: groupsPoints.groupPts) {
            val sortedParticipantPts = new ArrayList<>(groupPts.participantPoints);
            Collections.sort(sortedParticipantPts, ParticipantGroupPointsDTO.COMPARATOR);
            ParticipantDTO first = sortedParticipantPts.remove(sortedParticipantPts.size() - 1).participant;
            selectedParticipants.add(first);
            remainCandidateParticipantPts.addAll(sortedParticipantPts);
        }

        // complete extract remaining second,third of each group up to finalParticipantCount
        final int finalParticipantCount = tournament.finalParticipantCount();
        int selectRemainCount = finalParticipantCount - selectedParticipants.size();
        if (selectRemainCount > remainCandidateParticipantPts.size()) throw new IllegalStateException();
        Collections.sort(remainCandidateParticipantPts, ParticipantGroupPointsDTO.COMPARATOR);
        for(; selectRemainCount > 0; selectRemainCount--) {
            selectedParticipants.add(remainCandidateParticipantPts.remove(remainCandidateParticipantPts.size()-1).participant);
        }
        if (selectedParticipants.size() != finalParticipantCount) throw new IllegalStateException();

        // randomly sort participants in final phase grid
        Collections.shuffle(selectedParticipants);

        // re-resolve participantIds to participant ref
        val tournamentParticipants = participantRepo.findAllByTournament(tournament); // TOCHECK externalized/embedded documents
        val participantById = LsUtils.toMapByKey(tournamentParticipants, x -> x.id);
        tournament.finalPhaseParticipantGrid = LsUtils.map(selectedParticipants, x -> participantById.get(x.id));

        // create final phase matches
        int currNthPow = tournament.nthPowerFinalPhase;
        int currNthFinalMatchCount = Tournament.pow2(currNthPow);
        val finalPhaseMatches = new ArrayList<TournamentFinalPhaseMatch>(2*currNthFinalMatchCount);
        for(; currNthPow > 1; currNthPow--, currNthFinalMatchCount/=2) {
            // create matches
            int currMatchCount = currNthFinalMatchCount/2;
            val matches = new ArrayList<TournamentFinalPhaseMatch>(currMatchCount);
            for(int i = 0; i < currMatchCount; i++) {
                TournamentFinalPhaseMatch match = new TournamentFinalPhaseMatch(tournament, currNthPow, i);
                if (currNthPow == tournament.nthPowerFinalPhase) {
                    // fill participant in first tour of final matches
                    match.participant1 = tournament.finalPhaseParticipantGrid.get(2*i);
                    match.participant2 = tournament.finalPhaseParticipantGrid.get(2*i + 1);
                }

                match = tournamentFinalPhaseMatchRepo.save(match); // TOCHEK externalized/embedded docs?
                matches.add(match);
            }
        }
        tournament.finalPhaseMatches = finalPhaseMatches;

        // update db
        tournamentRepo.save(tournament);

        val res = new StartFinalMatchesPhaseResponseDTO(
                LsUtils.map(tournament.finalPhaseParticipantGrid, x -> dtoMapper.toParticipantDTO(x)));
        return res;
    }

    public FillFinalPhaseMatchResponseDTO fillFinalPhaseMatchScore(
            String tournamentId,
            FillFinalPhaseMatchRequestDTO req) {
        val tournament = getById(tournamentId);
        tournament.ensureState(TournamentState.FinalMatchPhase);
        List<TournamentFinalPhaseMatch> finalPhaseMatches = tournament.finalPhaseMatches;
        val match = Objects.requireNonNull(LsUtils.findFirst(finalPhaseMatches, x -> x.id.equals(req.matchId)
                && x.nthPowerPhase == req.nthPowPhase && x.matchGridRowIndex == req.matchGridRowIndex // redundant with id
                && x.participant1.id.equals(req.part1Id) && x.participant2.id.equals(req.part2Id) // redundant, for check only
        ));
        doFillMatchScore(match, req.setScores, req.matchEndTime);

        // update participant winner to next match
        if (match.nthPowerPhase > 1) {
            boolean part1Win = match.pts.part1Pts().points() > match.pts.part2Pts().points();
            val winnerPart = (part1Win) ? match.participant1 : match.participant2;
            // find next match
            int nextNthPow = match.nthPowerPhase-1;
            int nextMatchRowIndex = match.matchGridRowIndex/2;
            val nextMatch = Objects.requireNonNull(LsUtils.findFirst(finalPhaseMatches,
                    x -> x.nthPowerPhase == nextNthPow && x.matchGridRowIndex == nextMatchRowIndex));

            // update participant winner to next match as part1 or part2
            boolean upper = (match.matchGridRowIndex == 2*nextMatchRowIndex);
            if (upper) {
                nextMatch.participant1 = winnerPart;
            } else {
                nextMatch.participant2 = winnerPart;
            }

            tournamentFinalPhaseMatchRepo.save(nextMatch);
        }

        // update db
        tournamentFinalPhaseMatchRepo.save(match);
        tournamentRepo.save(tournament);

        val res = new FillFinalPhaseMatchResponseDTO();
        // TOADD
        return res;
    }

    public FinalPhaseMatchScoresDTO getFinalPhaseMatchScore(String tournamentId) {
        val tournament = getById(tournamentId);
        return dtoMapper.getFinalPhaseMatchScoresDTO(tournament);
    }

}
