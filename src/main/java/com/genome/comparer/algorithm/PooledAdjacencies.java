package com.genome.comparer.algorithm;

import java.util.ArrayList;
import java.util.List;

public class PooledAdjacencies {

    public ArrayList<Adjacency> adjacencies;

    public PooledAdjacencies(List<Genome> genomes) {
        adjacencies = new ArrayList<Adjacency>();
        for (Genome genome : genomes) {
            for (int[] index : genome.adjacencies) {
                boolean found = false;
                for (int i = 0; !found && i < adjacencies.size(); i++) {
                    int[] otherindex = adjacencies.get(i).index;
                    found = (index[0] == otherindex[0] && index[1] == otherindex[1]) ||
                        (index[0] == otherindex[1] && index[1] == otherindex[0]);
                }
                if (!found) {
                    //new adjacency, construct, and find the conflicts
                    int[] newindex = new int[]{index[0], index[1]};
                    Adjacency newadjacency = new Adjacency(newindex);
                    //System.out.println("New adjacency: ("+newadjacency.index[0]+","+newadjacency.index[1]+")");
                    for (Adjacency adjacency : adjacencies) {
                        if (adjacency.index[0] == newadjacency.index[0] ||
                            adjacency.index[0] == newadjacency.index[1] ||
                            adjacency.index[1] == newadjacency.index[0] ||
                            adjacency.index[1] == newadjacency.index[1]) {
                            adjacency.inconflict.add(newadjacency);
                            newadjacency.inconflict.add(adjacency);
                            // System.out.println("New conflict: (" + adjacency.index[0] + "," + adjacency.index[1]
                            //     + ") and (" + newadjacency.index[0] + "," + newadjacency.index[1] + ")");
                        }
                    }
                    adjacencies.add(newadjacency);
                }
            }
        }
    }

    public int[] fingerprint(Genome genome) {
        int[] list = new int[adjacencies.size()];
        // int counter = 0;
        for (int i = 0; i < list.length; i++) {
            int[] currentindex = adjacencies.get(i).index;
            boolean found = false;
            for (int j = 0; !found && j < genome.adjacencies.size(); j++) {
                int[] currentgenomeindex = genome.adjacencies.get(j);
                found = ((currentgenomeindex[0] == currentindex[0] &&
                    currentgenomeindex[1] == currentindex[1]) ||
                    (currentgenomeindex[0] == currentindex[1] &&
                        currentgenomeindex[1] == currentindex[0]));
            }
            if (found) {
                list[i] = 1;
                // counter++;
            } else {
                list[i] = 0;
            }
        }
        // System.out.println(
        //     "found adjacencies: " + counter + " number of adjacencies in genome: " + genome.adjacencies.size());
        return list;
    }

    public List<Adjacency> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(final ArrayList<Adjacency> adjacencies) {
        this.adjacencies = adjacencies;
    }
}
