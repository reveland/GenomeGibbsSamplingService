package com.genome.comparer.domain;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Test class for {@link Chromosome}
 */
public class ChromosomeTest {

    @Test
    public void testToString() {
        Chromosome chromosome = new Chromosome("label", Collections.singletonList(1));

        String actual = chromosome.toString();

        assertEquals("{\"label\":\"label\", \"adjacencies\":[1]}", actual);
    }

}
