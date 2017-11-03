package com.genome.comparer.core;

import com.genome.comparer.domain.Adjacency;
import com.genome.comparer.domain.Genome;

import java.util.ArrayList;
import java.util.List;

public class PooledAdjacencies {

    public List<Adjacency> adjacencies;

    public PooledAdjacencies(List<Genome> genomes) {

        adjacencies = new ArrayList<>();
        for (Genome genome : genomes) {
            for (int[] index : genome.getAdjacencies()) {
                boolean found = false;
                for (int i = 0; !found && i < adjacencies.size(); i++) {
                    int[] otherindex = adjacencies.get(i).index;
                    found = (index[0] == otherindex[0] && index[1] == otherindex[1]) ||
                            (index[0] == otherindex[1] && index[1] == otherindex[0]);
                }
                if (!found) {
                    int[] newindex = new int[]{index[0], index[1]};
                    Adjacency newadjacency = new Adjacency(newindex);
                    for (Adjacency adjacency : adjacencies) {
                        if (isConflicted(newadjacency, adjacency)) {
                            adjacency.inConflictWith.add(newadjacency);
                            newadjacency.inConflictWith.add(adjacency);
                        }
                    }
                    adjacencies.add(newadjacency);
                }
            }
        }
//        adjacencies = genomes.stream()
//                .flatMap(genome -> genome.getAdjacencies().stream())
//                .map(Adjacency::new)
//                .distinct()
//                .collect(Collectors.toList());
//        adjacencies.forEach(adjacency -> adjacency.setInconflict(adjacencies.stream()
//                .filter(other -> isConflicted(adjacency, other))
//                .collect(Collectors.toList())));
    }

    private boolean isConflicted(Adjacency adjacency, Adjacency other) {
        return adjacency != other && (
                other.index[0] == adjacency.index[0] ||
                        other.index[0] == adjacency.index[1] ||
                        other.index[1] == adjacency.index[0] ||
                        other.index[1] == adjacency.index[1]);
    }

    public int[] fingerprint(Genome genome) {
        return adjacencies.stream()
                .mapToInt(adjacency -> isContains(genome.getAdjacencies(), adjacency.index) ? 1 : 0)
                .toArray();
    }

    private boolean isContains(List<int[]> adjacencies, int[] adjacency) {
        return adjacencies.stream()
                .anyMatch(ints -> isTheSame(ints, adjacency));
    }

    private boolean isTheSame(int[] adj1, int[] adj2) {
        return (adj2[0] == adj1[0] && adj2[1] == adj1[1])
                || (adj2[0] == adj1[1] && adj2[1] == adj1[0]);
    }

    public List<Adjacency> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(List<Adjacency> adjacencies) {
        this.adjacencies = adjacencies;
    }

    @Override
    public String toString() {
        return adjacencies.toString();
    }
}
