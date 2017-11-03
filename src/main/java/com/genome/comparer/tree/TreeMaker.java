package com.genome.comparer.tree;

import com.genome.comparer.domain.Genome;
import com.genome.comparer.core.PooledAdjacencies;
import com.genome.comparer.core.Tree;

import java.util.List;

/**
 * Make Tree object
 */
public interface TreeMaker {
    Tree makeTree(double[][] m, String[] names);

    Tree makeTree(double[][] m, List<Genome> inputGenomes, PooledAdjacencies inputAdjacencies);
}
