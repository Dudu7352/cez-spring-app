package com.dudaj.cezspringapp.helper;

import java.util.Collection;

public class CollectionHelper {
    public static <T> boolean contains(Collection<T> collection, ContainsPredicate<T> predicate) {
        for (T element : collection) {
            if (predicate.run(element)) {
                return true;
            }
        }
        return false;
    }

    public interface ContainsPredicate<T> {
        boolean run(T obj);
    }
}
