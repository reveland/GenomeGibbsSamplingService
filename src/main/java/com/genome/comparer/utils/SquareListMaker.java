package com.genome.comparer.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.Square;

public class SquareListMaker {

    public List<Square> make(List<Chromosome> original) {
        return original.stream()
                .flatMap(chromosome -> chromosome.getAdjacencies().stream()
                        .map(adjacency -> new Square(adjacency, chromosome.getLabel())))
                .collect(Collectors.toList());
    }
}
