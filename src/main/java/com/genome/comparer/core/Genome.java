package com.genome.comparer.core;

import java.util.ArrayList;
import java.util.List;

import com.genome.comparer.domain.Chromosome;

public class Genome {

    public String name;
    public ArrayList<int[]> adjacencies;
    public int[] fingerprint;
    public List<Chromosome> original;
    private boolean circular = false;

    public Genome(String name, List<int[]> adjacencies) {
        this.name = name;
        this.adjacencies = new ArrayList<>(adjacencies);
    }

    public Genome(List<int[]> adjacencies, String name) {
        this.name = name;
        this.adjacencies = new ArrayList<>(adjacencies);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<int[]> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(final List<int[]> adjacencies) {
        this.adjacencies = new ArrayList<>(adjacencies);
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(final boolean circular) {
        this.circular = circular;
    }

    @Override
    public String toString() {
        return "Genome{" +
            "name='" + name + '\'' +
            "}\n";
    }

}
