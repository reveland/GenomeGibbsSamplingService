package com.genome.comparer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adjacency {

    public int[] index;
    public List<Adjacency> inConflictWith;

    public Adjacency(int[] index) {
        this.index = index;
        this.inConflictWith = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adjacency adjacency = (Adjacency) o;
        int[] adj1 = adjacency.index;
        int[] adj2 = this.index;

        return ((adj2[0] == adj1[0] && adj2[1] == adj1[1])
                || (adj2[0] == adj1[1] && adj2[1] == adj1[0]));

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(index);
        result = 31 * result + (inConflictWith != null ? inConflictWith.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return '[' + String.valueOf(index[0]) + ',' + String.valueOf(index[1]) + ']';
    }

}
