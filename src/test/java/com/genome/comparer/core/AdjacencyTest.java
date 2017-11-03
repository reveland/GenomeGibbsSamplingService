package com.genome.comparer.core;

import com.genome.comparer.domain.Adjacency;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link Adjacency}
 */
public class AdjacencyTest {

    private static final int[] TEST_ADJACENCIES = {1, 2};

    @Test
    public void testGetAdjacency() {
        Adjacency adjacency = new Adjacency(TEST_ADJACENCIES);

        int[] actual = adjacency.index;

        assertEquals(TEST_ADJACENCIES[0], actual[0]);
        assertEquals(TEST_ADJACENCIES[1], actual[1]);
    }

    @Test
    public void testToString() {
        Adjacency adjacency = new Adjacency(TEST_ADJACENCIES);
        String expected = "[1,2]";

        String actual = adjacency.toString();

        assertEquals(expected, actual);
    }
}
