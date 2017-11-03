package com.genome.comparer.core;

import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.Genome;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Test class for {@link Genome}
 */
public class GenomeTest {

    private static final String GENOME_NAME_1 = "genome_name_1";
    private static final int[] ADJACENCY_1 = {0, 1};
    private static final int[] ADJACENCY_2 = {2, 3};

    @Test
    public void testToString() {
        Genome genome = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Chromosome chromosome1 = new Chromosome("label", Collections.singletonList(1));
        Chromosome chromosome2 = new Chromosome("label", Collections.singletonList(1));
        genome.setOriginal(Lists.newArrayList(chromosome1, chromosome2));
        genome.setFingerprint(new int[]{0, 1, 0});

        String actual = genome.toString();

        assertEquals("{\n" +
                "\"name\":\"genome_name_1\",\n" +
                "\"adjacencies\":[[0,1], [2,3]],\n" +
                "\"fingerprint\":[0, 1, 0],\n" +
                "\"original\":[\n" +
                "{\"label\":\"label\", \"adjacencies\":[1]}, \n" +
                "{\"label\":\"label\", \"adjacencies\":[1]}],\n" +
                "\"circular\":false\n" +
                "}", actual);
    }

}
