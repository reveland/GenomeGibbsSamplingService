package com.genome.comparer.algorithm;

import java.util.List;

import com.genome.comparer.domain.Chromosome;

public class Genome {

    private String name;
    private List<int[]> adjacencies;
    public int[] fingerprint;
    public List<Chromosome> original;
    private boolean circular = false;

    public Genome(List<int[]> adjacencies, String name) {
        this.name = name;
        this.adjacencies = adjacencies;
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
        this.adjacencies = adjacencies;
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
