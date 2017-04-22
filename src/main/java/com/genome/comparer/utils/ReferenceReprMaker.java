package com.genome.comparer.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;

@Service
public class ReferenceReprMaker {

    private static int i = 0;

    public List<RefSquare> make(Genome genome) {
        i = 0;
        return genome.original.stream()
            .flatMap(chromosome -> chromosome.getAdjacencies().stream()
                .map(adjacency -> createRefSquare(chromosome, adjacency)))
            .collect(Collectors.toList());

        //        List<RefSquare> RefSquares = new ArrayList<>();
        //        List<Chromosome> originalGenome = genome.original;
        //        for (int i = 0; i < originalGenome.size(); i++) {
        //            Chromosome originalChromosome = originalGenome.get(i);
        //            for (int j = 0; j < originalChromosome.getAdjacencies().size(); j++) {
        //                RefSquare square = new RefSquare(originalChromosome.getAdjacencies().get(j),
        //                    originalChromosome.getLabel());
        //                RefSquares.add(square);
        //            }
        //        }
        //        return RefSquares;
    }

    private static RefSquare createRefSquare(final Chromosome chromosome, final Integer adjacency) {
        return new RefSquare(adjacency, chromosome.getLabel(), i++);
    }
}
