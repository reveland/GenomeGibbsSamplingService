package com.genome.comparer.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Adjacency {

    public int[] index;
    public ArrayList<Adjacency> inconflict;

    public Adjacency(int[] index) {
        this.index = index;
        inconflict = new ArrayList<>();
    }

    public int[] getAdjacency() {
        return index;
    }

    @Override
    public String toString() {
        return '(' + String.valueOf(index[0]) + ',' + String.valueOf(index[1]) + ')';
    }
}
