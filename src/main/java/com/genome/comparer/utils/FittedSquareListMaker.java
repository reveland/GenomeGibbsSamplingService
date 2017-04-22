package com.genome.comparer.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;
import com.genome.comparer.domain.Square;
import com.genome.comparer.domain.SquareList;

@Service
public class FittedSquareListMaker {

    private boolean isResult;

    public List<SquareList> fit(List<Chromosome> originalChromosomes, List<RefSquare> refSquares) {
        return originalChromosomes.stream()
            .map(chromosome -> fitChromosome(chromosome, refSquares))
            .collect(Collectors.toList());
        //        List<SquareList> result = new ArrayList<>();
        //        for (Chromosome originalChromosome : originalChromosomes) {
        //            SquareList chromosome = fitChromosome(originalChromosome, refSquares);
        //            result.add(chromosome);
        //        }
        //        return result;
    }

    private SquareList fitChromosome(Chromosome originalChromosome, List<RefSquare> refSquares) {
        List<Square> squares = originalChromosome.getAdjacencies().stream()
            .map(adjacency -> getSquare(adjacency, refSquares))
            .collect(Collectors.toList());
        return new SquareList(originalChromosome.getLabel(), squares);
        //        List<Square> result = new ArrayList<>();
        //        for (int i = 0; i < originalChromosome.getAdjacencies().size(); i++) {
        //            Square square = getSquare(originalChromosome.getAdjacencies().get(i), refSquares);
        //            result.add(square);
        //        }
        //        return new SquareList(originalChromosome.getLabel(), result);
    }

    private Square getSquare(Integer adjacency, List<RefSquare> refSquareList) {

        return refSquareList.stream()
            .filter(refSquare -> isEquals(adjacency, refSquare) || isInvertedEquals(adjacency, refSquare))
            .map(refSquare -> new Square(refSquare.getLabel(), isInvertedEquals(adjacency, refSquare),
                refSquare.getId()))
            .findFirst().orElse(new Square("0", false, 0));
        //
        //        Square result = new Square("0", false);
        //        for (RefSquare refSquare : refSquareList) {
        //            result = fitSquare(adjacency, refSquare);
        //            if (isResult) {
        //                break;
        //            }
        //        }
        //        return result;
    }

    private boolean isEquals(final Integer adjacency, final RefSquare refSquare) {
        return adjacency.equals(refSquare.getAdjacency());
    }

    private boolean isInvertedEquals(final Integer adjacency, final RefSquare refSquare) {
        return adjacency.equals(refSquare.getAdjacency() * (-1));
    }

    //    private Square fitSquare(Integer orig, RefSquare refSquare) {
    //        Square result = new Square("0", false);
    //        isResult = false;
    //        Integer reference = refSquare.getAdjacency();
    //        if (orig.equals(reference)) {
    //            result = new Square(refSquare.getLabel(), false);
    //            isResult = true;
    //        } else if (orig.equals(reference * (-1))) {
    //            result = new Square(refSquare.getLabel(), true);
    //            isResult = true;
    //        }
    //        return result;
    //    }
}
