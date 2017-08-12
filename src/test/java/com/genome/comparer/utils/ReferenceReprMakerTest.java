package com.genome.comparer.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.genome.comparer.core.Genome;
import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceReprMakerTest {

    private static final String CHROMOSOME_LABEL_1 = "chromosomeLabel1";
    private static final String CHROMOSOME_LABEL_2 = "chromosomeLabel2";
    private static final String CHROMOSOME_LABEL_3 = "chromosomeLabel3";

    @InjectMocks
    private ReferenceReprMaker underTest;

    @Test
    public void make() {
        Genome genome = new Genome(Collections.emptyList(), "genomeName");
        genome.original = createChromosomes();
        List<RefSquare> expected = getExpectedRefSquares();

        List<RefSquare> actual = underTest.make(genome);

        Assert.assertEquals(expected, actual);
    }

    private List<RefSquare> getExpectedRefSquares() {
        return Arrays.asList(new RefSquare(10, CHROMOSOME_LABEL_1),
            new RefSquare(-20, CHROMOSOME_LABEL_1),
            new RefSquare(30, CHROMOSOME_LABEL_1),
            new RefSquare(-40, CHROMOSOME_LABEL_1),
            new RefSquare(50, CHROMOSOME_LABEL_1),
            new RefSquare(60, CHROMOSOME_LABEL_2),
            new RefSquare(-70, CHROMOSOME_LABEL_2),
            new RefSquare(80, CHROMOSOME_LABEL_2),
            new RefSquare(-90, CHROMOSOME_LABEL_2),
            new RefSquare(100, CHROMOSOME_LABEL_2),
            new RefSquare(1, CHROMOSOME_LABEL_3),
            new RefSquare(-2, CHROMOSOME_LABEL_3),
            new RefSquare(3, CHROMOSOME_LABEL_3),
            new RefSquare(-4, CHROMOSOME_LABEL_3),
            new RefSquare(5, CHROMOSOME_LABEL_3)
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