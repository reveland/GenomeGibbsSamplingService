package com.genome.comparer.domain;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Test class for {@link RefSquare}
 */
public class RefSquareTest {

    @Test
    public void testToString() {
        RefSquare refSquare = new RefSquare(1, "asd", 2);

        String actual = refSquare.toString();

        assertEquals("{\"id\":2, \"adjacency\":1, \"label\":\"asd\"}", actual);
    }
}
