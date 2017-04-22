package com.genome.comparer.utils;

import static com.genome.comparer.service.SyntenyReCounter.makeChains;

import java.util.ArrayList;
import java.util.List;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.domain.Chromosome;

public class FingerprintToGenomeConverter {

    private PooledAdjacencies adjacencies;

    public FingerprintToGenomeConverter(PooledAdjacencies pooledAdjacencies) {
        this.adjacencies = pooledAdjacencies;
    }

    public Genome convert(String name, int[] fingerprint) {
        Genome genome;
        if (fingerprint.length != adjacencies.getAdjacencies().size()) {
            throw new RuntimeException("Fingerprint and adjanencies hasn1t got the same size!");
        }
        ArrayList<int[]> originalPairs = new ArrayList<>();
        for (int i = 0; i < fingerprint.length; i++) {
            if (fingerprint[i] == 1) {
                originalPairs.add(adjacencies.getAdjacencies().get(i).getAdjacency());
            }
        }

        genome = new Genome(originalPairs, name);
        genome.fingerprint = fingerprint;

        ArrayList<int[]> reversedPairs = new ArrayList<>();
        for (int[] pair : originalPairs) {
            reversedPairs.add(pair);
        }

        List<List<Integer>> chromosomes = makeChains(reversedPairs);
        genome.setCircular(testCircles(chromosomes));
        List<Chromosome> genomeOriginal = new ArrayList<>();
        for (int i = 0; i < chromosomes.size(); i++) {
            Chromosome chromosomeOriginal = new Chromosome(Integer.toString(i + 1), chromosomes.get(i));
            genomeOriginal.add(chromosomeOriginal);
        }
        genome.original = genomeOriginal;
        return genome;
    }

    private boolean testCircles(List<List<Integer>> chromosomes) {
        boolean hasCircle = false;
        for (List<Integer> chromosome : chromosomes) {
            if (chromosome.get(0).equals(chromosome.get(chromosome.size() - 1))) {
                hasCircle = true;
                break;
            }
        }
        return hasCircle;
    }
}
