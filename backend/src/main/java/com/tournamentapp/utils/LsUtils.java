package com.tournamentapp.utils;

import com.tournamentapp.domain.Participant;
import com.tournamentapp.domain.TournamentGroup;
import lombok.val;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class LsUtils {

    /** private to force all static */
    private LsUtils() {}

    public static <TSrc,TDest> List<TDest> map(Collection<TSrc> src,
                                               Function<TSrc,TDest> func) {
        if (src == null) {
            return new ArrayList<>(0);
        }
        val res = new ArrayList<TDest>(src.size());
        for(val elt : src) {
            res.add(func.apply(elt));
        }
        return res;
    }

    public static <TSrc,TDest> List<TDest> map(TSrc[] src,
                                               Function<TSrc,TDest> func) {
        if (src == null) {
            return new ArrayList<>(0);
        }
        val res = new ArrayList<TDest>(src.length);
        for(val elt : src) {
            res.add(func.apply(elt));
        }
        return res;
    }

    public static <T> List<T> filter(Collection<T> src,
                                     Predicate<T> pred) {
        if (src == null) {
            return new ArrayList<>(0);
        }
        val res = new ArrayList<T>(src.size());
        for(val elt : src) {
            if (pred.test(elt)) {
                res.add(elt);
            }
        }
        res.trimToSize();
        return res;
    }

    public static <T> T findFirst(Collection<T> src, Predicate<T> pred) {
        if (src == null || src.isEmpty()) return null;
        for(val elt : src) {
            if (pred.test(elt)) {
                return elt;
            }
        }
        return null;
    }

    public static <TKey,T> LinkedHashMap<TKey,T> toMapByKey(Collection<T> src, Function<T,TKey> keyExtractFunc) {
        val res = new LinkedHashMap<TKey,T>();
        for(val elt : src) {
            TKey key = keyExtractFunc.apply(elt);
            if (key != null) {
                res.put(key, elt);
            }
        }
        return res;
    }
}
