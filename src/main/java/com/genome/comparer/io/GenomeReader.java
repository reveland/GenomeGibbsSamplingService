package com.genome.comparer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.domain.Chromosome;

public class GenomeReader {

    public static ArrayList<Genome> read(String filename) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(new File(filename)));
        ArrayList<Genome> genomes = new ArrayList<>();
        String s;
        s = bf.readLine();
        String[] temp = s.split("\t");
        String currentName = temp[0].substring(1, temp[0].length() - 1);
        ArrayList<int[]> adjacencies = new ArrayList<>();
        List<Chromosome> original = new ArrayList<>();
        String chromosomeName = "";
        while ((s = bf.readLine()) != null) {
            if (s.length() != 0 && s.charAt(0) != '>') {
                List<Integer> chromosomeAdj = new ArrayList<>();
                Chromosome chromosome;
                if (s.length() != 0 && s.charAt(0) == '#') {
                    chromosomeName = s.substring(5, s.length());
                } else if (s.length() != 0 && s.charAt(0) != '#') {
                    String[] syntenys = s.split(" ");
                    int orig = Integer.parseInt(syntenys[0]);
                    chromosomeAdj.add(orig);
                    for (int i = 0; i < syntenys.length - 2; i++) {
                        int a = Integer.parseInt(syntenys[i]);
                        int b = Integer.parseInt(syntenys[i + 1]);
                        int[] adjacency = new int[2];
                        chromosomeAdj.add(b);
                        if (a > 0) {
                            adjacency[0] = 2 * a;
                        } else {
                            adjacency[0] = -2 * a - 1;
                        }
                        if (b > 0) {
                            adjacency[1] = 2 * b - 1;
                        } else {
                            adjacency[1] = -2 * b;
                        }
                        adjacencies.add(adjacency);
                    }
                    chromosome = new Chromosome(chromosomeName, chromosomeAdj);
                    original.add(chromosome);
                }
            } else if (s.length() != 0 && s.charAt(0) == '>') {

                Genome currentGenome = new Genome(adjacencies, currentName);
                currentGenome.original = original;
                currentGenome.setCircular(false);
                genomes.add(currentGenome);
                temp = s.split("\t");
                currentName = temp[0].substring(1, temp[0].length() - 1);
                adjacencies = new ArrayList<>();
                original = new ArrayList<>();
            }
        }

        Genome currentGenome = new Genome(adjacencies, currentName);
        currentGenome.original = original;
        genomes.add(currentGenome);
        bf.close();
        return genomes;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Genome> genomes = read("./data/HMMRDCOC_100_300.perm");
        System.out.println("ready");
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(genomes);
        System.out.println(
            "number of pooled deprecatedAdjacencies: " + pooledAdjacencies.getAdjacencies().size());
        for (Genome genome : genomes) {
            genome.fingerprint = pooledAdjacencies.fingerprint(genome);
        }
        // Hamming distances
        for (Genome genomex : genomes) {
            System.out.print(genomex.getName() + " ");
            for (Genome genomey : genomes) {
                int hamming = 0;
                for (int i = 0; i < genomex.fingerprint.length; i++) {
                    if (genomex.fingerprint[i] != genomey.fingerprint[i]) {
                        hamming++;
                    }
                }
                System.out.print(hamming + " ");
            }
            System.out.println();
        }
    }
}
