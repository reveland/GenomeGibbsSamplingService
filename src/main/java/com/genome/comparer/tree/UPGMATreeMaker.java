package com.genome.comparer.tree;

import java.util.List;

import com.genome.comparer.core.Genome;
import com.genome.comparer.core.PooledAdjacencies;
import com.genome.comparer.core.Tree;
import com.genome.comparer.core.TreeNode;

/**
 * Unweighted Pair Group Method with Arithmetic Mean Tree
 */
public class UPGMATreeMaker implements TreeMaker  {

    private PooledAdjacencies adjacencies;
    private List<Genome> genomes;

    @Override
    public Tree makeTree(double[][] m, String[] names) {
        Tree gt = new Tree();

        TreeNode[] nodes = new TreeNode[names.length];
        int[] sizes = new int[names.length];
        double[] heights = new double[names.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = 1;
            heights[i] = 0;
        }
        TreeNode last = null;
        for (int iter = 0; iter < m.length - 1; iter++) {
            double min = Double.MAX_VALUE;
            int x = 0;
            int y = 0;
            for (int i = 1; i < m.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (m[i][j] < min) {
                        min = m[i][j];
                        x = i;
                        y = j;
                    }
                }
            }
            if (nodes[x] == null) {
                nodes[x] = new TreeNode();
                nodes[x].name = names[x];
                nodes[x].owner = gt;
            }
            if (nodes[y] == null) {
                nodes[y] = new TreeNode();
                nodes[y].name = names[y];
                nodes[x].owner = gt;
            }
            double h = min / 2;
            TreeNode tn = new TreeNode();
            tn.leftChild = nodes[x];
            nodes[x].parent = tn;
            nodes[x].evolDist = h - heights[x];
            tn.rightChild = nodes[y];
            nodes[y].parent = tn;
            nodes[y].evolDist = h - heights[y];
            nodes[x] = tn;
            for (int k = 0; k < m.length; k++) {
                m[k][x] = (m[k][x] * sizes[x] + m[k][y] * sizes[y]) / (sizes[x] + sizes[y]);
                m[x][k] = m[k][x];
            }
            for (int k = 0; k < m.length; k++) {
                m[k][y] = Double.MAX_VALUE;
                m[y][k] = Double.MAX_VALUE;
            }
            sizes[x] += sizes[y];
            heights[x] = h;
            last = nodes[x];
        }
        gt.root = last;
        gt.useMidPointRoot();
        return gt;
    }

    @Override
    public Tree makeTree(double[][] m, List<Genome> inputGenomes, PooledAdjacencies inputAdjacencies) {

        adjacencies = inputAdjacencies;
        genomes = inputGenomes;

        Tree gt = new Tree();
        gt.adjacencies = adjacencies;

        TreeNode[] nodes = new TreeNode[genomes.size()];
        int[] sizes = new int[genomes.size()];
        double[] heights = new double[genomes.size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = 1;
            heights[i] = 0;
        }
        TreeNode last = null;
        for (int iter = 0; iter < m.length - 1; iter++) {
            double min = Double.MAX_VALUE;
            int x = 0;
            int y = 0;
            for (int i = 1; i < m.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (m[i][j] < min) {
                        min = m[i][j];
                        x = i;
                        y = j;
                    }
                }
            }
            if (nodes[x] == null) {
                nodes[x] = new TreeNode();
                nodes[x].name = genomes.get(x).getName();
                nodes[x].owner = gt;
                nodes[x].fingerprint = adjacencies.fingerprint(genomes.get(x));
            }
            if (nodes[y] == null) {
                nodes[y] = new TreeNode();
                nodes[y].name = genomes.get(y).getName();
                nodes[y].owner = gt;
                nodes[y].fingerprint = adjacencies.fingerprint(genomes.get(y));
            }
            double h = min / 2;
            TreeNode tn = new TreeNode();
            tn.owner = gt;
            tn.leftChild = nodes[x];
            nodes[x].parent = tn;
            nodes[x].evolDist = h - heights[x];
            tn.rightChild = nodes[y];
            nodes[y].parent = tn;
            nodes[y].evolDist = h - heights[y];
            nodes[x] = tn;
            for (int k = 0; k < m.length; k++) {
                m[k][x] = (m[k][x] * sizes[x] + m[k][y] * sizes[y]) / (sizes[x] + sizes[y]);
                m[x][k] = m[k][x];
            }
            for (int k = 0; k < m.length; k++) {
                m[k][y] = Double.MAX_VALUE;
                m[y][k] = Double.MAX_VALUE;
            }
            sizes[x] += sizes[y];
            heights[x] = h;
            last = nodes[x];
            gt.root = last;
        }
        gt.root = last;
        gt.useMidPointRoot();
        gt.root.owner = gt;
        gt.fitch();
        for (int i = 0; i < gt.root.fingerprint.length; i++) {
            gt.root.calculateSankoff(i);
        }
        // System.out.println("makeTree done");
        return gt;
    }

//    public static void main(String[] args) {
//        double m[][] =
//                {
//                        {0, 2, 4, 6, 6, 8},
//                        {2, 0, 4, 6, 6, 8},
//                        {4, 4, 0, 6, 6, 8},
//                        {6, 6, 6, 0, 4, 8},
//                        {6, 6, 6, 4, 0, 8},
//                        {8, 8, 8, 8, 8, 0}
//                };
//        String[] names = {"A", "B", "C", "D", "E", "F"};
//        UPGMATreeMaker ut = new UPGMATreeMaker();
//        GuideTree gt = ut.makeTree(m, names);
//        gt.printTree();
//        System.out.println();
//        System.out.println(gt.newickString());
//    }

}
