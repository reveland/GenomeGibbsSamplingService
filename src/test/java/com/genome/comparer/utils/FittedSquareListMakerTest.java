package com.genome.comparer.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;
import com.genome.comparer.domain.Square;
import com.genome.comparer.domain.SquareList;

@RunWith(MockitoJUnitRunner.class)
public class FittedSquareListMakerTest {

    private static final String CHROMOSOME_LABEL_1 = "chromosomeLabel1";
    private static final String CHROMOSOME_LABEL_2 = "chromosomeLabel2";
    private static final String CHROMOSOME_LABEL_3 = "chromosomeLabel3";

    @InjectMocks
    private FittedSquareListMaker underTest;

    @Test
    public void testFit() throws Exception {
        List<RefSquare> refSquares = createRefSquares();
        List<Chromosome> chromosomes = createChromosomes();
        List<SquareList> expected = createExpectedSquareLists();

        List<SquareList> actual = underTest.fit(chromosomes, refSquares);

        Assert.assertEquals(expected, actual);
    }

    private List<SquareList> createExpectedSquareLists() {
        SquareList squareList1 = new SquareList(CHROMOSOME_LABEL_1,
            Arrays.asList(
                new Square(CHROMOSOME_LABEL_1, false),
                new Square(CHROMOSOME_LABEL_1, false),
                new Square(CHROMOSOME_LABEL_1, false),
                new Square(CHROMOSOME_LABEL_1, false),
                new Square(CHROMOSOME_LABEL_1, false)));
        SquareList squareList2 = new SquareList(CHROMOSOME_LABEL_2,
            Arrays.asList(
                new Square(CHROMOSOME_LABEL_2, false),
                new Square(CHROMOSOME_LABEL_2, false),
                new Square(CHROMOSOME_LABEL_2, false),
                new Square(CHROMOSOME_LABEL_2, false),
                new Square(CHROMOSOME_LABEL_2, false)));
        SquareList squareList3 = new SquareList(CHROMOSOME_LABEL_3,
            Arrays.asList(
                new Square(CHROMOSOME_LABEL_3, false),
                new Square(CHROMOSOME_LABEL_3, false),
                new Square(CHROMOSOME_LABEL_3, false),
                new Square(CHROMOSOME_LABEL_3, false),
                new Square(CHROMOSOME_LABEL_3, false)));

        return Arrays.asList(squareList1, squareList2, squareList3);
    }

    private List<RefSquare> createRefSquares() {
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