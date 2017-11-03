package com.genome.comparer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.genome.comparer.domain.Chromosome;

public class Genome {

    private String name;
    private List<int[]> adjacencies;
    private boolean circular;
    private int[] fingerprint;
    private List<Chromosome> original;

    public Genome(String name, List<int[]> adjacencies) {
        this.name = name;
        this.adjacencies = adjacencies;
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

    public int[] getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(int[] fingerprint) {
        this.fingerprint = fingerprint;
    }

    public List<Chromosome> getOriginal() {
        return original;
    }

    public void setOriginal(List<Chromosome> original) {
        this.original = original;
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
