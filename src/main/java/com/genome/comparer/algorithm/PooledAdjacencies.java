package com.genome.comparer.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledAdjacencies {

    private final Logger LOGGER = LoggerFactory.getLogger(PooledAdjacencies.class);

    private List<Adjacency> adjacencies;

    public PooledAdjacencies(List<Genome> genomes) {
        adjacencies = new ArrayList<>();
        for (Genome genome : genomes) {
            for (int[] currentAdjacency : genome.getAdjacencies()) {
                if (!adjacencies.contains(new Adjacency(currentAdjacency))) {
                    addNewAdjacency(currentAdjacency);
                }
            }
        }
    }

    public int[] fingerprint(Genome genome) {
        logAdjacenciesInfo(genome);

        List<int[]> genomeAdjacencies = genome.getAdjacencies();
        //                List<Integer> fingerprint = adjacencies.stream()
        //                    .map(adjacency -> genomeAdjacencies.contains(adjacency) ? 1 : 0)
        //                    .collect(Collectors.toList());
        List<Integer> fingerprint = new ArrayList<>();
        for (Adjacency adjacency : adjacencies) {
            if (genomeAdjacencies.contains(adjacency.getAdjacency())) {
                fingerprint.add(1);
            } else {
                fingerprint.add(0);
            }
        }
        return fingerprint.stream().mapToInt(Integer::byteValue).toArray();
    }

    private void logAdjacenciesInfo(final Genome genome) {
        //        LOGGER.info("adjacencies in the pool: {}", adjacencies.stream()
        //            .map(conflictedAdjacency -> "(" + conflictedAdjacency.getExtremityA() + " " + conflictedAdjacency.getExtremityB() + ")")
        //            .reduce(String::concat).orElse(""));
        //        LOGGER.info("adjacencies in the genome: {}", genome.getAdjacencies().stream()
        //            .map(conflictedAdjacency -> "(" + conflictedAdjacency.getExtremityA() + " " + conflictedAdjacency.getExtremityB() + ")")
        //            .reduce(String::concat).orElse(""));
    }

    private void addNewAdjacency(final int[] currentAdjacency) {
        Adjacency newAdjacency = new Adjacency(currentAdjacency);
        setConflicts(newAdjacency);
        adjacencies.add(newAdjacency);

        //        LOGGER.info("New adjacency: ({}, {})", currentAdjacency.getExtremityA(), currentAdjacency.getExtremityB());
    }

    private void setConflicts(final Adjacency newAdjacency) {
        for (Adjacency adjacency : adjacencies) {
            if (adjacency.isConflictedWith(newAdjacency)) {
                setConflict(newAdjacency, adjacency);
            }
        }
    }

    private void setConflict(final Adjacency newAdjacency,
        final Adjacency adjacency) {
        adjacency.addConflict(newAdjacency);
        newAdjacency.addConflict(adjacency);

        //        LOGGER.info("New conflict: ({}, {}) <-> ({}, {})", adjacency.getExtremityA(), adjacency.getExtremityB(),
        //            newAdjacency.getExtremityA(), newAdjacency.getExtremityB());
    }

    public List<Adjacency> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(final ArrayList<Adjacency> adjacencies) {
        this.adjacencies = adjacencies;
    }
}
