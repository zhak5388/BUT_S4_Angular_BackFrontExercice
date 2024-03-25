package com.tournamentapp.rest.dto.match;

import lombok.val;

import java.util.Collection;

public record MatchPoints(
        ParticipantMatchPoints part1Pts, ParticipantMatchPoints part2Pts
) {
    public static final int MATCH_WIN_POINTS = 1;
    public static final int SET_POINTS = 11;
    public static final int SET_DIFF_POINTS = 2;
    public static final int MIN_SETS_WIN = 3;

    public static MatchPoints toMatchPoints(Collection<SetScoreDTO> setScores) {
        int set1Win = 0, set2Win = 0;
        int goalAverage1Points = 0, goalAverage2Points = 0;
        for(val score: setScores) {
            int s1 = score.score1, s2 = score.score2;
            ensureValidSetScore(s1, s2);
            if (s1 > s2) {
                set1Win++;
                goalAverage2Points += s2;
            } else {
                set2Win++;
                goalAverage1Points += s1;
            }
        }
        int part1Points = 0, part2Points = 0;
        int goalAverage1Sets = 0, goalAverage2Sets = 0;
        boolean win1 = (set1Win > set2Win);
        if (win1) {
            if (set1Win != MIN_SETS_WIN) throw new IllegalArgumentException();
            part1Points = MATCH_WIN_POINTS;
            goalAverage2Sets = set2Win;
        } else {
            if (set2Win != MIN_SETS_WIN) throw new IllegalArgumentException();
            part2Points = MATCH_WIN_POINTS;
            goalAverage1Sets = set1Win;
        }

        return new MatchPoints(
                new ParticipantMatchPoints(part1Points, goalAverage1Sets, goalAverage1Points),
                new ParticipantMatchPoints(part2Points, goalAverage2Sets, goalAverage2Points));
    }

    public static void ensureValidSetScore(int s1, int s2) {
        if (s1 < s2) {
            ensureValidSetScoreWin1(s2, s1);
        } else {
            ensureValidSetScoreWin1(s1, s2);
        }
    }

    private static void ensureValidSetScoreWin1(int s1, int s2) {
        if (s1 == SET_POINTS && s2 <= s1-SET_DIFF_POINTS) {
            // set won with 11:x, example: 11:8
        } else if (s1 > SET_POINTS && (s1-s2) == SET_DIFF_POINTS) {
            // set won with 2 points of difference, example: 15:13
        } else {
            throw new IllegalArgumentException("bad set score " + s1 + "-" + s2);
        }
    }

}
