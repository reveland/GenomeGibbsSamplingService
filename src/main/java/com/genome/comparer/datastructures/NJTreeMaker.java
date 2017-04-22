package com.genome.comparer.datastructures;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.algorithm.Tree;
import com.genome.comparer.algorithm.TreeNode;

public class NJTreeMaker {

    private final static Logger LOGGER = LoggerFactory.getLogger(NJTreeMaker.class);

    private PooledAdjacencies adjacencies;
    private List<Genome> genomes;

    public Tree makeTree(double[][] m, String[] names) {
        Tree gt = new Tree();

        TreeNode[] nodes = new TreeNode[names.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new TreeNode();
            nodes[i].name = names[i];
            nodes[i].owner = gt;
        }
        TreeNode last = null;
        boolean[] used = new boolean[m.length];
        double[] v = new double[m.length];
        for (int r = m.length - 2; r >= 0; r--) {
            Arrays.fill(v, 0);
            for (int i = 0; i < v.length; i++) {
                if (!used[i]) {
                    for (int j = 0; j < m[i].length; j++) {
                        if (!used[j]) {
                            v[i] += m[i][j];
                        }
                    }
                }
            }
            double min = Double.MAX_VALUE, val;
            int x = 0;
            int y = 0;
            for (int i = 1; i < m.length; i++) {
                if (!used[i]) {
                    for (int j = 0; j < i; j++) {
                        if (!used[j]) {
                            if (min > (val = r * m[i][j] - v[i] - v[j])) {
                                x = j;
                                y = i;
                                min = val;
                            }
                        }
                    }
                }
            }
            TreeNode tn = new TreeNode();
            tn.name = "(" + nodes[x].name + "," + nodes[y].name + ")";
            tn.owner = gt;
            tn.leftChild = nodes[x];
            tn.leftChild.parent = tn;
            tn.rightChild = nodes[y];
            tn.rightChild.parent = tn;
            // calculating the chosen node's distances from the newly created node
            double distX = (m[x][y] + (r > 0 ? (v[x] - v[y]) / r : 0)) / 2;
            double distY = m[x][y] - distX;
            if (distX < 0) {
                System.err.println("TreeAlgorithm - NJ - DistX : NEGATIVE DISTANCE");
            }
            if (distY < 0) {
                System.err.println("TreeAlgorithm - NJ - DistY : NEGATIVE DISTANCE");
            }
            nodes[x].evolDist = distX;
            nodes[y].evolDist = distY;
            nodes[x] = tn;
            last = nodes[x];
            used[y] = true;

            for (int k = 0; k < m.length; k++)
                if (!used[k])
                    m[k][x] = m[x][k] = (m[k][x] + m[k][y] - m[x][y]) / 2;
        }
        gt.root = last;
        gt.useMidPointRoot();

        return gt;
    }

    public Tree makeTree(double[][] m, List<Genome> inputGenomes, PooledAdjacencies inputAdjacencies) {
        genomes = inputGenomes;
        adjacencies = inputAdjacencies;

        Tree gt = new Tree();
        gt.adjacencies = adjacencies;

        TreeNode[] nodes = new TreeNode[genomes.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new TreeNode();
            nodes[i].name = genomes.get(i).getName();
            nodes[i].owner = gt;
            nodes[i].fingerprint = adjacencies.fingerprint(genomes.get(i));
        }
        TreeNode last = null;
        boolean[] used = new boolean[m.length];
        double[] v = new double[m.length];
        for (int r = m.length - 2; r >= 0; r--) {
            Arrays.fill(v, 0);
            for (int i = 0; i < v.length; i++) {
                if (!used[i]) {
                    for (int j = 0; j < m[i].length; j++) {
                        if (!used[j]) {
                            v[i] += m[i][j];
                        }
                    }
                }
            }
            double min = Double.MAX_VALUE, val;
            int x = 0;
            int y = 0;
            for (int i = 1; i < m.length; i++) {
                if (!used[i]) {
                    for (int j = 0; j < i; j++) {
                        if (!used[j]) {
                            if (min > (val = r * m[i][j] - v[i] - v[j])) {
                                x = j;
                                y = i;
                                min = val;
                            }
                        }
                    }
                }
            }
            TreeNode tn = new TreeNode();
            // tn.name = "[" + nodes[x].name + ";" + nodes[y].name + "]";
            tn.owner = gt;
            tn.leftChild = nodes[x];
            tn.leftChild.parent = tn;
            tn.rightChild = nodes[y];
            tn.rightChild.parent = tn;
            // calculating the chosen node's distances from the newly created node
            double distX = (m[x][y] + (r > 0 ? (v[x] - v[y]) / r : 0)) / 2;
            double distY = m[x][y] - distX;
            if (distX < 0) {
                System.err.println("TreeAlgorithm - NJ - DistX : NEGATIVE DISTANCE");
            }
            if (distY < 0) {
                System.err.println("TreeAlgorithm - NJ - DistY : NEGATIVE DISTANCE");
            }
            nodes[x].evolDist = distX;
            nodes[y].evolDist = distY;
            nodes[x] = tn;
            last = nodes[x];
            used[y] = true;
            gt.root = last;
            gt.root.owner = gt;

            // recalculating distances
            for (int k = 0; k < m.length; k++)
                if (!used[k])
                    m[k][x] = m[x][k] = (m[k][x] + m[k][y] - m[x][y]) / 2;
        }
        gt.root = last;
        gt.root.owner = gt;

        gt.useMidPointRoot();

        gt.fitch();
        gt.root.owner = gt;
        for (int i = 0; i < gt.root.fingerprint.length; i++) {
            gt.root.calculateSankoff(i);
        }
        return gt;
    }
}
