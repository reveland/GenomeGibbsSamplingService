package com.genome.comparer.io;

import com.genome.comparer.domain.Genome;
import com.genome.comparer.domain.Chromosome;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class GenomeReaderTest {

    @Test
    public void testRead() throws Exception {

        ArrayList<Genome> result = GenomeReader.read("./data/test_genome_data.perm");

        ArrayList<Genome> expected = new ArrayList<>();

        Chromosome chr1 = createChromosome("1", Arrays.asList(1, 2, 3));
        Chromosome chr2 = createChromosome("2", Arrays.asList(4, 5, 6));
        Chromosome chr3 = createChromosome("3", Arrays.asList(7, 8, 9, 10));
        List<Chromosome> original = Arrays.asList(chr1, chr2, chr3);
        List<int[]> adjacencies = createAdjacencies(2, 3, 4, 5, 8, 9, 10, 11, 14, 15, 16, 17, 18, 19);
        expected.add(createGenome("g1", adjacencies, null, original, false));

        chr1 = createChromosome("1", Arrays.asList(-2, -1, 3, 7));
        chr2 = createChromosome("2", Arrays.asList(8, 9, 10));
        chr3 = createChromosome("3", Arrays.asList(-6, -5, -4));
        original = Arrays.asList(chr1, chr2, chr3);
        adjacencies = createAdjacencies(3, 2, 1, 5, 6, 13, 16, 17, 18, 19, 11, 10, 9, 8);
        expected.add(createGenome("g2", adjacencies, null, original, false));

        chr1 = createChromosome("1", Arrays.asList(1, 2));
        chr2 = createChromosome("2", Arrays.asList(3, 4));
        chr3 = createChromosome("3", Arrays.asList(5, 6, 7));
        Chromosome chr4 = createChromosome("4", Arrays.asList(8, 9, 10));
        original = Arrays.asList(chr1, chr2, chr3, chr4);
        adjacencies = createAdjacencies(2, 3, 6, 7, 10, 11, 12, 13, 16, 17, 18, 19);
        expected.add(createGenome("g3", adjacencies, null, original, false));

        //assertEquals(expected.toString(), result.toString());
    }

    private Chromosome createChromosome(String s, List<Integer> adjacencies) {
        return new Chromosome(s, adjacencies);
    }

    private List<int[]> createAdjacencies(int... ints) {
        List<int[]> adjacencies = new ArrayList<>();
        for (int i = 0; i < ints.length; i += 2) {
            adjacencies.add(new int[]{ints[i], ints[i + 1]});
        }
        return adjacencies;
    }

    private Genome createGenome(String name, List<int[]> adjacencies, int[] fingerprint, List<Chromosome> original, boolean circular) {
        Genome genome = new Genome(name, adjacencies);
        genome.setCircular(circular);
        genome.setFingerprint(fingerprint);
        genome.setOriginal(original);
        return genome;
    }
}