package com.genome.comparer.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Adjacency {

    private int[] adjacency;

    public List<Adjacency> inConflictWith;

    public Adjacency(final int[] adjacency) {
        this.adjacency = adjacency;
        this.inConflictWith = new ArrayList<>();
    }

    public boolean isConflictedWith(Adjacency otherAdjacency) {
        return this.adjacency[0] == otherAdjacency.adjacency[0]
            || this.adjacency[0] == otherAdjacency.adjacency[1]
            || this.adjacency[1] == otherAdjacency.adjacency[0]
            || this.adjacency[1] == otherAdjacency.adjacency[1];
    }

    public void addConflict(final Adjacency otherAdjacency) {
        inConflictWith.add(otherAdjacency);
    }

    private boolean isTheSame(Adjacency other) {
        return adjacency[0] == other.adjacency[0]
            && adjacency[1] == other.adjacency[1]
            || adjacency[0] == other.adjacency[1]
            && adjacency[1] == other.adjacency[0];
    }

    @Override
    public boolean equals(final Object o) {
        Adjacency other = (Adjacency) o;

        return this.isTheSame(other);
    }

    @Override
    public int hashCode() {
        return adjacency[0] * 31 + adjacency[1] * 31;
    }

    public int[] getAdjacency() {
        return adjacency;
    }
}
