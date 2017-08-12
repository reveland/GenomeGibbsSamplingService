package com.genome.comparer.datastructures;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.algorithm.Tree;

import java.util.List;

/**
 * Make Tree object
 */
public interface TreeMaker {
    Tree makeTree(double[][] m, String[] names);

    Tree makeTree(double[][] m, List<Genome> inputGenomes, PooledAdjacencies inputAdjacencies);
}
