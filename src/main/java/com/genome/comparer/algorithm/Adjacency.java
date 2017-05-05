package com.genome.comparer.algorithm;

import java.util.ArrayList;

public class Adjacency {

    public int[] index;
    public ArrayList<Adjacency> inconflict;

    public Adjacency(int[] index) {
        this.index = index;
        inconflict = new ArrayList<Adjacency>();
    }

    public int[] getAdjacency() {
        return index;
    }
}
