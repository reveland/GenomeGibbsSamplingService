package com.genome.comparer.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.genome.comparer.core.Genome;
import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;

@Service
public class ReferenceReprMaker {

    public List<RefSquare> make(List<Chromosome> original) {
        return original.stream()
                .flatMap(chromosome -> chromosome.getAdjacencies().stream()
                        .map(adjacency -> createRefSquare(chromosome, adjacency)))
                .collect(Collectors.toList());
    }

    private static RefSquare createRefSquare(final Chromosome chromosome, final Integer adjacency) {
        return new RefSquare(adjacency, chromosome.getLabel());
    }
}
