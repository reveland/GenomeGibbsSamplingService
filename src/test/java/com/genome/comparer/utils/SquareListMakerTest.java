package com.genome.comparer.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.genome.comparer.domain.Genome;
import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.Square;

@RunWith(MockitoJUnitRunner.class)
public class SquareListMakerTest {

    private static final String CHROMOSOME_LABEL_1 = "chromosomeLabel1";
    private static final String CHROMOSOME_LABEL_2 = "chromosomeLabel2";
    private static final String CHROMOSOME_LABEL_3 = "chromosomeLabel3";

    @InjectMocks
    private SquareListMaker underTest;

    @Test
    public void make() {
        Genome genome = new Genome("genomeName", Collections.emptyList());
        genome.setOriginal(createChromosomes());
        List<Square> expected = getExpectedRefSquares();

        List<Square> actual = underTest.make(genome.getOriginal());

        Assert.assertEquals(expected, actual);
    }

    private List<Square> getExpectedRefSquares() {
        return Arrays.asList(new Square(10, CHROMOSOME_LABEL_1),
            new Square(-20, CHROMOSOME_LABEL_1),
            new Square(30, CHROMOSOME_LABEL_1),
            new Square(-40, CHROMOSOME_LABEL_1),
            new Square(50, CHROMOSOME_LABEL_1),
            new Square(60, CHROMOSOME_LABEL_2),
            new Square(-70, CHROMOSOME_LABEL_2),
            new Square(80, CHROMOSOME_LABEL_2),
            new Square(-90, CHROMOSOME_LABEL_2),
            new Square(100, CHROMOSOME_LABEL_2),
            new Square(1, CHROMOSOME_LABEL_3),
            new Square(-2, CHROMOSOME_LABEL_3),
            new Square(3, CHROMOSOME_LABEL_3),
            new Square(-4, CHROMOSOME_LABEL_3),
            new Square(5, CHROMOSOME_LABEL_3)
        );
    }

    private List<Chromosome> createChromosomes() {
        List<Integer> adjacencies1 = Arrays.asList(10, -20, 30, -40, 50);
        List<Integer> adjacencies2 = Arrays.asList(60, -70, 80, -90, 100);
        List<Integer> adjacencies3 = Arrays.asList(1, -2, 3, -4, 5);
        Chromosome chromosome1 = new Chromosome(CHROMOSOME_LABEL_1, adjacencies1);
        Chromosome chromosome2 = new Chromosome(CHROMOSOME_LABEL_2, adjacencies2);
        Chromosome chromosome3 = new Chromosome(CHROMOSOME_LABEL_3, adjacencies3);
        return Arrays.asList(chromosome1, chromosome2, chromosome3);
    }

}