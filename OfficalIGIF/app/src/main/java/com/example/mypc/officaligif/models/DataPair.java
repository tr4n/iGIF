package com.example.mypc.officaligif.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

public class DataPair<F, S> {
    public  @Nullable F first;
    public  @Nullable S second;

    /**
     * Constructor for a Pair.
     *
     * @param first the first object in the Pair
     * @param second the second object in the pair
     */
    public DataPair(@Nullable F first, @Nullable S second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Object#equals(Object)} methods.
     *
     * @param o the {@link android.support.v4.util.Pair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered
     *         equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof android.support.v4.util.Pair)) {
            return false;
        }
        android.support.v4.util.Pair<?, ?> p = (android.support.v4.util.Pair<?, ?>) o;
        return objectsEqual(p.first, first) && objectsEqual(p.second, second);
    }

    private static boolean objectsEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    @Override
    public String toString() {
        return "Pair{" + String.valueOf(first) + " " + String.valueOf(second) + "}";
    }


}
