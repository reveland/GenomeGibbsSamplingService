package com.genome.comparer.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.genome.comparer.domain.Square;
import com.genome.comparer.domain.repr.BlockRepr;

@RunWith(MockitoJUnitRunner.class)
public class GenomeReprMakerTest {

    @InjectMocks
    private GenomeReprMaker underTest;

    @Test
    public void testGetBlockReprs() {
        List<Square> squares = createSquares();
        List<BlockRepr> expected = getExpectedBlockReprs();

        List<BlockRepr> actual = underTest.getBlockReprs(squares);

        Assert.assertEquals(expected, actual);
    }

    private List<BlockRepr> getExpectedBlockReprs() {
        return Arrays.asList(
            new BlockRepr(0, "1", 4, false),
            new BlockRepr(1, "3", 2, true),
            new BlockRepr(2, "3", 2, false),
            new BlockRepr(3, "2", 4, true)
        );
    }

    private List<Square> createSquares() {
        return Arrays.asList(
            new Square("1", false),
            new Square("1", false),
            new Square("1", false),
            new Square("1", false),
            new Square("3", true),
            new Square("3", true),
            new Square("3", false),
            new Square("3", false),
            new Square("2", true),
            new Square("2", true),
            new Square("2", true),
            new Square("2", true));
    }

}