package com.genome.comparer.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.genome.comparer.domain.Genome;
import com.genome.comparer.io.GenomeReader;
import com.genome.comparer.utils.FingerprintToGenomeConverter;

public class Tree {

    public static Random random = new Random(1200);

    public PooledAdjacencies adjacencies;
    public TreeNode root;
    // not part of the original core
    public List<Genome> genomes;

    public Tree() {

    }

    public Tree(ArrayList<Genome> inputGenomes) {
        // not part of the original core
        genomes = inputGenomes;

        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(inputGenomes);
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        for (Genome genome : inputGenomes) {
            TreeNode current = new TreeNode();
            current.owner = this;
            current.fingerprint = pooledAdjacencies.fingerprint(genome);
            current.name = genome.getName();
            nodes.add(current);
        }
        while (nodes.size() > 1) {
            //find the closest nodes
            int min = pooledAdjacencies.adjacencies.size();
            int a = 0;
            int b = 1;
            for (int i = 0; i < nodes.size(); i++) {
                int[] x = nodes.get(i).fingerprint;
                for (int j = i + 1; j < nodes.size(); j++) {
                    int[] y = nodes.get(j).fingerprint;
                    int sum = 0;
                    for (int k = 0; k < x.length; k++) {
                        sum += (x[k] == y[k] ? 0 : 1);
                    }
                    if (sum < min) {
                        min = sum;
                        a = i;
                        b = j;
                    }
                }
            }
            //merge these two nodes
            TreeNode x = nodes.get(a);
            TreeNode y = nodes.get(b);
            nodes.remove(b);
            nodes.remove(a);
            Tree cherry = new Tree();
            TreeNode root = new TreeNode();
            root.owner = cherry;
            x.owner = cherry;
            y.owner = cherry;
            root.leftChild = x;
            root.rightChild = y;
            x.parent = root;
            y.parent = root;
            cherry.adjacencies = pooledAdjacencies;
            cherry.root = root;
            cherry.fitch();
            x.owner = this;
            y.owner = this;
            root.owner = this;
            root.name = "(" + x.name + "," + y.name + ")";
            nodes.add(root);
        }
        this.root = nodes.get(0);
    }

    public void crosscheck() {
        root.crosscheck(this);
    }

    /**
     * This method does the Fitch core on the tree
     */
    public void fitch() {
        root.fitchUp();
        // System.out.println("Fitch checkscore: " + root.fitchcheckscore);
        root.fitchDown();
    }

    public int parsimonyScore() {
        return root.parsimonyScore();
    }

    public void gibbsSampling() {
        int x = random.nextInt(adjacencies.adjacencies.size());
        root.calculateSankoff(x);
//         System.out.println("numer of optimal labelling: " +
//             ((root.sankoffscore[0] <= root.sankoffscore[1] ? root.sankoffsum[0] : 0) +
//                 (root.sankoffscore[1] <= root.sankoffscore[0] ? root.sankoffsum[1] : 0)) + ", individual scores: " +
//             root.sankoffsum[0] + ", " + root.sankoffsum[1]);
        root.sankoffmark[0] = root.sankoffscore[0] <= root.sankoffscore[1];
        root.sankoffmark[1] = root.sankoffscore[1] <= root.sankoffscore[0];
        root.select(x);
    }

    /**
     * for debugging reasons only
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Tree tree = new Tree(GenomeReader.read("./data/HMMRDCOC_100_300.perm"));
        System.out.println(tree.root.name);
    }

    // not part of the original core

    /**
     * Replaces the root of the tree to a position where the means of the branch
     * lengths on either side of the root are equal. Recursive.
     */
    public void useMidPointRoot() {
        if (root.hasLeftChild() && root.hasRightChild()) {
            TreeNode lChild = root.leftChild;
            TreeNode rChild = root.rightChild;
            double lDist = lChild.evolDist;
            double rDist = rChild.evolDist;
            double lAvg = lChild.getAvgLenToLeaves();
            double rAvg = rChild.getAvgLenToLeaves();
            // check if we are done
            double difference = Math.abs((lAvg + lDist) - (rAvg + rDist));
            if (difference < 0.001) {
                return;
            }
            // do we need to replace the root or is it enough to adjust the
            // top-level distances?
            TreeNode moveRootTowardsNode;
            TreeNode otherChildNode;
            if (lDist + lAvg > rDist + rAvg) {
                // System.out.println("moving left");
                moveRootTowardsNode = lChild;
                otherChildNode = rChild;
                double tmp = lAvg;
                lAvg = rAvg;
                rAvg = tmp;
                tmp = lDist;
                lDist = rDist;
                rDist = tmp;
            } else {
                // System.out.println("moving right");
                moveRootTowardsNode = rChild;
                otherChildNode = lChild;
            }
            if (difference < moveRootTowardsNode.evolDist * 2) {
                // no need to create new node, just adjust distances:
                double newDistToNode = ((rAvg + lAvg + lDist + rDist) / 2) - rAvg;
                double newDistToOtherNode = ((rAvg + lAvg + lDist + rDist) / 2) - lAvg;
                if (newDistToNode < 0 || newDistToOtherNode < 0) {
                    System.err.println("TreeAlgorithm: NEGATIVE DISTANCE");
                }
                // set distances
                moveRootTowardsNode.evolDist = newDistToNode;
                otherChildNode.evolDist = newDistToOtherNode;
            } else {
                // choose another section to place root to:
                if (!moveRootTowardsNode.hasLeftChild() || !moveRootTowardsNode.hasRightChild()) {
                    System.err.println("Node has no children!");
                    System.exit(3);
                }
                double lDist2 = moveRootTowardsNode.leftChild.evolDist;
                double lAvg2 = moveRootTowardsNode.leftChild.getAvgLenToLeaves();
                double rDist2 = moveRootTowardsNode.rightChild.evolDist;
                double rAvg2 = moveRootTowardsNode.rightChild.getAvgLenToLeaves();

                boolean toLeft;
                if (lDist2 + lAvg2 > rDist2 + rAvg2) {
                    toLeft = true;
                } else {
                    toLeft = false;
                }

                // reverse some parent-child relationships
                otherChildNode.parent = moveRootTowardsNode; // instead of
                // oldRoot
                moveRootTowardsNode.evolDist = 0.0;
                otherChildNode.evolDist = otherChildNode.evolDist + moveRootTowardsNode.evolDist;
                if (otherChildNode.evolDist < 0) {
                    System.err.println("TreeAlgorithm 2 : NEGATIVE DISTANCE");
                }

                TreeNode newRoot = new TreeNode(); // this will be the new
                // root-candidate
                newRoot.name = "newroot";
                TreeNode tmpNode;
                if (toLeft) {
                    tmpNode = moveRootTowardsNode.leftChild;
                    moveRootTowardsNode.leftChild = otherChildNode;
                    moveRootTowardsNode.parent = newRoot;
                    tmpNode.parent = newRoot;
                    newRoot.leftChild = tmpNode;
                    newRoot.rightChild = moveRootTowardsNode;
                } else {
                    tmpNode = moveRootTowardsNode.rightChild;
                    moveRootTowardsNode.rightChild = otherChildNode;
                    moveRootTowardsNode.parent = newRoot;
                    tmpNode.parent = newRoot;
                    newRoot.leftChild = moveRootTowardsNode;
                    newRoot.rightChild = tmpNode;
                }
                // set initial distances
                newRoot.evolDist = 0.0;
                moveRootTowardsNode.resetAvgLenToLeaves();
                otherChildNode.resetAvgLenToLeaves();

                root = newRoot;
                useMidPointRoot();
            }
        } else {
            System.err.println("ERROR: root has no children ! " + "leftChild:" + root.hasLeftChild() + "  rightChild:"
                    + root.hasRightChild());
            System.exit(4);
        }
    }

    // not part of the original core
    public List<Genome> getGenomes(FingerprintToGenomeConverter fingerprintConverter) {
        List<Genome> allGenomes = new ArrayList<>(genomes);
        return root.getGenomes(allGenomes, fingerprintConverter);
    }

    @Override
    public String toString() {
        return "{" +
                "\n\"adjacencies\":" + adjacencies +
                ",\n\"genomes\":" + genomes.stream().map(genome ->
                "\n" + genome).collect(Collectors.toList()) +
                ",\n\"tree\":" + root.toString() +
                "\n}";
    }
}
