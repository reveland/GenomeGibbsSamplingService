package com.genome.comparer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.genome.comparer.domain.Chromosome;

public class Genome {

    public String name;
    public ArrayList<int[]> adjacencies;
    public int[] fingerprint;
    public List<Chromosome> original;
    private boolean circular;

    public Genome(String name, List<int[]> adjacencies) {
        this.name = name;
        this.adjacencies = new ArrayList<>(adjacencies);
        this.circular = false;
    }

    public Genome(List<int[]> adjacencies, String name) {
        this.name = name;
        this.adjacencies = new ArrayList<>(adjacencies);
        this.circular = false;
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
        return "{" +
                "\n\"name\":" + "\"" + name + "\"" +
                ",\n\"adjacencies\":" + adjacencies.stream().map(ints ->
                "[" + ints[0] + "," + ints[1] + "]").collect(Collectors.toList()) +
                ",\n\"fingerprint\":" + Arrays.toString(fingerprint) +
                ",\n\"original\":" + original.stream().map(chr ->
                "\n" + chr).collect(Collectors.toList()) +
                ",\n\"circular\":" + circular +
                "\n}";
    }
}
