package com.genome.comparer.core;

import com.genome.comparer.domain.Adjacency;
import com.genome.comparer.domain.Genome;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link PooledAdjacencies}
 */
public class PooledAdjacenciesTest {

    private static final String GENOME_NAME_1 = "genome_name_1";
    private static final String GENOME_NAME_2 = "genome_name_2";
    private static final String GENOME_NAME_3 = "genaom_name_3";
    private static final int[] ADJACENCY_1 = {0, 1};
    private static final int[] ADJACENCY_2 = {2, 3};
    private static final int[] ADJACENCY_3 = {4, 5};
    private static final int[] ADJACENCY_4 = {6, 7};
    private static final int[] ADJACENCY_5 = {8, 9};
    private static final int[] CONFLICTED_ADJACENCY_1 = {0, 10};
    private static final int[] CONFLICTED_ADJACENCY_2 = {1, 11};

    @Test
    public void testFingerprintWhenThePoolContainsTheGenome() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2));

        int[] actual = pooledAdjacencies.fingerprint(genome_1);

        assertArrayEquals(new int[]{1, 1, 0, 0}, actual);
    }

    @Test
    public void testFingerprintWhenThePoolNotContainsTheGenome() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2));
        Genome genome_3 = new Genome(GENOME_NAME_3, Arrays.asList(ADJACENCY_1, ADJACENCY_4));

        int[] actual = pooledAdjacencies.fingerprint(genome_3);

        assertArrayEquals(new int[]{1, 0, 0, 1}, actual);
    }

    @Test
    public void testFingerprintWhenThereIsConflict() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2));
        Genome genome_3 = new Genome(GENOME_NAME_3, Arrays.asList(ADJACENCY_1, ADJACENCY_4));

        int[] actual = pooledAdjacencies.fingerprint(genome_3);

        assertArrayEquals(new int[]{1, 0, 0, 1}, actual);
    }

    @Test
    public void testFingerprintWhenThePoolNotContainsTheGenomeAndHasUnknownAdjacency() {
        // TODO is it could be happening?
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2));
        Genome genome_3 = new Genome(GENOME_NAME_3, Arrays.asList(ADJACENCY_1, ADJACENCY_5));

        int[] actual = pooledAdjacencies.fingerprint(genome_3);

        assertArrayEquals(new int[]{1, 0, 0, 0}, actual);
    }

    @Test
    public void testGetAdjacenciesSetTheConflictsGood() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        Genome genome_3 = new Genome(GENOME_NAME_3, Arrays.asList(CONFLICTED_ADJACENCY_1, CONFLICTED_ADJACENCY_2));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2, genome_3));

        List<Adjacency> actual = pooledAdjacencies.getAdjacencies();

        assertEquals(Lists.newArrayList(new Adjacency(CONFLICTED_ADJACENCY_1), new Adjacency(CONFLICTED_ADJACENCY_2)).toString(), actual.get(0).getInconflict().toString());
        assertEquals(Collections.singletonList(new Adjacency(ADJACENCY_1)).toString(), actual.get(4).getInconflict().toString());
        assertEquals(Collections.singletonList(new Adjacency(ADJACENCY_1)).toString(), actual.get(5).getInconflict().toString());
    }


    @Test
    public void testToString() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_1, genome_2));

        String actual = pooledAdjacencies.toString();

        assertEquals("[[0,1], [2,3], [4,5], [6,7]]", actual);
    }

    // @Test TODO should't the pool be a set? or at least ordered?
    public void testToStringWhenTheOrderIsDifferent() {
        Genome genome_1 = new Genome(GENOME_NAME_1, Arrays.asList(ADJACENCY_1, ADJACENCY_2));
        Genome genome_2 = new Genome(GENOME_NAME_2, Arrays.asList(ADJACENCY_3, ADJACENCY_4));
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(Arrays.asList(genome_2, genome_1));

        String actual = pooledAdjacencies.toString();

        assertEquals("[[0,1], [2,3], [4,5], [6,7]]", actual);
    }

}
