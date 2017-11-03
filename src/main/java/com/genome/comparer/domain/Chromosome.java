package com.genome.comparer.domain;

import java.util.List;

public class Chromosome {

    private String label;
    private List<Integer> adjacencies;

    public Chromosome(String label, List<Integer> adjacencies) {
        this.label = label;
        this.adjacencies = adjacencies;
    }

    public String getLabel() {
        return label;
    }

    public List<Integer> getAdjacencies() {
        return adjacencies;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAdjacencies(List<Integer> adjacencies) {
        this.adjacencies = adjacencies;
    }

    @Override
    public String toString() {
        return "{\"label\":\"" + label + "\", \"adjacencies\":" + adjacencies + "}";
    }
}
