package com.genome.comparer.domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link Square}
 */
public class SquareTest {

    @Test
    public void testToString() {
        Square square = new Square(1, "asd");

        String actual = square.toString();

        assertEquals("{\"adjacency\":1, \"label\":\"asd\"}", actual);
    }
}
