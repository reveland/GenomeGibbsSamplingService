package com.genome.comparer.domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link Square}
 */
public class SquareTest {

    @Test
    public void testToString() {
        Square square = new Square("asd", false, 1);

        String actual = square.toString();

        assertEquals("{\"id\":1, \"label\":\"asd\", \"inverted\":false}", actual);
    }

}
