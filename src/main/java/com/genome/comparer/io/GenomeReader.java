package com.genome.comparer.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.genome.comparer.domain.Genome;
import com.genome.comparer.core.PooledAdjacencies;
import com.genome.comparer.domain.Chromosome;

public class GenomeReader {

    // private static final Logger LOGGER = LoggerFactory.getLogger(GenomeReader.class);

    public static ArrayList<Genome> read(String filename) throws IOException {
        BufferedReader reader = getBufferedReader(filename);
        ArrayList<Genome> genomes = new ArrayList<>();
        ArrayList<int[]> adjacencies = new ArrayList<>();
        List<Chromosome> original = new ArrayList<>();
        String line = reader.readLine();
        String genomeName = getGenomeName(line);
        String chromosomeName = "";
        while ((line = reader.readLine()) != null) {
            if (isGenomeNameLine(line)) {
                Genome currentGenome = new Genome(adjacencies, genomeName);
                currentGenome.original = original;
                currentGenome.setCircular(false);
                // LOGGER.debug(String.valueOf(currentGenome));
                genomes.add(currentGenome);
                // preparing for read the new genome data
                genomeName = getGenomeName(line);
                adjacencies = new ArrayList<>();
                original = new ArrayList<>();
            } else if (notBlankLine(line)) {
                List<Integer> chromosomeAdj = new ArrayList<>();
                if (isChromosomeNameLine(line)) {
                    chromosomeName = getChromosomeName(line);
                } else {
                    String[] syntenys = line.split(" ");
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
                    original.add(new Chromosome(chromosomeName, chromosomeAdj));
                }
            }
        }

        Genome currentGenome = new Genome(adjacencies, genomeName);
        currentGenome.original = original;
        currentGenome.setCircular(false);
        // LOGGER.debug(String.valueOf(currentGenome));
        genomes.add(currentGenome);
        reader.close();
        return genomes;
    }

    private static boolean notBlankLine(String line) {
        return line.length() != 0;
    }

    private static String getChromosomeName(String line) {
        return line.substring(5, line.length());
    }

    private static boolean isGenomeNameLine(String line) {
        return notBlankLine(line) && line.charAt(0) == '>';
    }

    private static boolean isChromosomeNameLine(String line) {
        return notBlankLine(line) && line.charAt(0) == '#';
    }

    private static String getGenomeName(String line) {
        String[] temp = line.split("\t");
        return temp[0].substring(1, temp[0].length() - 1);
    }

    private static BufferedReader getBufferedReader(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(filename)));
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Genome> genomes = read("./data/test_genome_data.perm");
        System.out.println("ready");
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(genomes);
        System.out.println("number of pooled deprecatedAdjacencies: " + pooledAdjacencies.getAdjacencies().size());
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
