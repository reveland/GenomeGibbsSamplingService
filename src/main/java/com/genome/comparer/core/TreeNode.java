package com.genome.comparer.core;

import java.util.Arrays;
import java.util.List;

import com.genome.comparer.utils.FingerprintToGenomeConverter;
import com.genome.comparer.utils.Utils;

public class TreeNode {

    // not part of the original core
    public double evolDist; // distance from its parent
    // not part of the original core
    private double avgLenToLeaves = -1;
    // not part of the original core
    private int numOfLeaves = -1;

    public TreeNode parent;
    public TreeNode leftChild;
    public TreeNode rightChild;
    public Tree owner;
    public int[] fingerprint;
    public int[] fitchauxiliary;

    public int[] sankoffscore;
    public int[] sankoffsum;
    public boolean[] sankoffmark;

    public String name;

    int fitchcheckscore;

    public double logscore;

    public TreeNode() {
        sankoffscore = new int[2];
        sankoffsum = new int[2];
        sankoffmark = new boolean[2];
    }

    public void crosscheck(Tree tree) {
        if (owner != tree) {
            throw new Error("My owner is not this tree!");
        }
        if (parent != null) {
            if (parent.leftChild != this && parent.rightChild != this) {
                throw new Error("my parent does not have me!");
            }
        }
        if (leftChild != null) {
            if (leftChild.parent != this) {
                throw new Error("I am not the parent of my leftChild!");
            }
            leftChild.crosscheck(tree);
        }
        if (rightChild != null) {
            if (rightChild.parent != this) {
                throw new Error("I am not the parent of my rightChild!");
            }
            rightChild.crosscheck(tree);
        }
        if (leftChild == null && rightChild != null) {
            throw new Error("I am inconsistent: I have no leftChild but I do have a rightChild!");
        }
        if (rightChild == null && leftChild != null) {
            throw new Error("I am inconsistent: I have no rightChild but I do have a leftChild!");
        }
    }

    public void fitchUp() {
        if (leftChild != null) {
            leftChild.fitchUp();
            rightChild.fitchUp();
            fitchcheckscore = leftChild.fitchcheckscore + rightChild.fitchcheckscore;
            fitchauxiliary = new int[leftChild.fitchauxiliary.length];
            fingerprint = new int[fitchauxiliary.length];
            /*
             *      0  1  2
			 *   0  0  2  0
			 *   1  2  1  1
			 *   2  0  1  2
			 * 
			 */
            for (int i = 0; i < fitchauxiliary.length; i++) {
                if ((leftChild.fitchauxiliary[i] == 2 && rightChild.fitchauxiliary[i] == 2) ||
                        (leftChild.fitchauxiliary[i] == 0 && rightChild.fitchauxiliary[i] == 1) ||
                        (leftChild.fitchauxiliary[i] == 1 && rightChild.fitchauxiliary[i] == 0)) {
                    fitchauxiliary[i] = 2;
                    if ((leftChild.fitchauxiliary[i] == 0 && rightChild.fitchauxiliary[i] == 1) ||
                            (leftChild.fitchauxiliary[i] == 1 && rightChild.fitchauxiliary[i] == 0)) {
                        fitchcheckscore++;
                    }
                } else if ((leftChild.fitchauxiliary[i] == 0 && rightChild.fitchauxiliary[i] == 0) ||
                        (leftChild.fitchauxiliary[i] == 0 && rightChild.fitchauxiliary[i] == 2) ||
                        (leftChild.fitchauxiliary[i] == 2 && rightChild.fitchauxiliary[i] == 0)) {
                    fitchauxiliary[i] = 0;
                } else {
                    fitchauxiliary[i] = 1;
                }
            }

        } else {
            fitchauxiliary = new int[fingerprint.length];
            for (int i = 0; i < fingerprint.length; i++) {
                fitchauxiliary[i] = fingerprint[i];
            }
            fitchcheckscore = 0;
        }
    }

    public int parsimonyScore() {
        int sum = 0;
        if (leftChild != null) {
            sum += leftChild.parsimonyScore();
            sum += rightChild.parsimonyScore();
        }
        if (parent != null) {
            for (int i = 0; i < fingerprint.length; i++) {
                sum += (fingerprint[i] == parent.fingerprint[i] ? 0 : 1);
            }

        }
        return sum;
    }

    public void fitchDown() {
        if (parent == null) {
            for (int i = 0; i < fingerprint.length; i++) {
                if (fitchauxiliary[i] == 1) {
                    fingerprint[i] = 1;
                } else {
                    fingerprint[i] = 0;
                }
            }
            leftChild.fitchDown();
            rightChild.fitchDown();
        } else {
            if (leftChild != null) {
                for (int i = 0; i < fingerprint.length; i++) {
                    if (fitchauxiliary[i] != 2) {
                        fingerprint[i] = fitchauxiliary[i];
                    } else {
                        fingerprint[i] = parent.fingerprint[i];
                    }
                }
                leftChild.fitchDown();
                rightChild.fitchDown();
            }
        }
    }

    public void calculateSankoff(int x) {
        if (leftChild != null) {
            leftChild.calculateSankoff(x);
            rightChild.calculateSankoff(x);
            sankoffscore[0] = Math.min(leftChild.sankoffscore[0], leftChild.sankoffscore[1] + 1) +
                    Math.min(rightChild.sankoffscore[0], rightChild.sankoffscore[1] + 1);
            sankoffscore[1] = Math.min(leftChild.sankoffscore[0] + 1, leftChild.sankoffscore[1]) +
                    Math.min(rightChild.sankoffscore[0] + 1, rightChild.sankoffscore[1]);
            int sum = 0;
            sum += (leftChild.sankoffscore[0] <= leftChild.sankoffscore[1] + 1 ? leftChild.sankoffsum[0] : 0);
            sum += (leftChild.sankoffscore[0] >= leftChild.sankoffscore[1] + 1 ? leftChild.sankoffsum[1] : 0);

            int sum1 = 0;
            sum1 += (rightChild.sankoffscore[0] <= rightChild.sankoffscore[1] + 1 ? rightChild.sankoffsum[0] : 0);
            sum1 += (rightChild.sankoffscore[0] >= rightChild.sankoffscore[1] + 1 ? rightChild.sankoffsum[1] : 0);

            sankoffsum[0] = sum * sum1;

            boolean is1inConflict = false;
            List<Adjacency> conflicts = owner.adjacencies.adjacencies.get(x).inconflict;
            for (int i = 0; !is1inConflict && i < conflicts.size(); i++) {
                int y = owner.adjacencies.adjacencies.indexOf(conflicts.get(i));
                is1inConflict = (fingerprint[y] == 1);
            }
            if (is1inConflict) {
                sankoffsum[1] = 0;
            } else {
                sum = 0;
                sum += (leftChild.sankoffscore[0] + 1 <= leftChild.sankoffscore[1] ? leftChild.sankoffsum[0] : 0);
                sum += (leftChild.sankoffscore[0] + 1 >= leftChild.sankoffscore[1] ? leftChild.sankoffsum[1] : 0);

                sum1 = 0;
                sum1 += (rightChild.sankoffscore[0] + 1 <= rightChild.sankoffscore[1] ? rightChild.sankoffsum[0] : 0);
                sum1 += (rightChild.sankoffscore[0] + 1 >= rightChild.sankoffscore[1] ? rightChild.sankoffsum[1] : 0);

                sankoffsum[1] = sum * sum1;
            }

        } else {
            if (fingerprint[x] == 0) {
                sankoffscore[0] = 0;
                sankoffsum[0] = 1;
                sankoffscore[1] = 10000;
                sankoffsum[1] = 0;
            } else {
                sankoffscore[0] = 10000;
                sankoffsum[0] = 0;
                sankoffscore[1] = 0;
                sankoffsum[1] = 1;
            }
        }
    }

    public void select(int x) {
        if (leftChild != null) {
            sankoffsum[0] *= (sankoffmark[0] ? 1 : 0);
            sankoffsum[1] *= (sankoffmark[1] ? 1 : 0);
            int p = Utils.random.nextInt(sankoffsum[0] + sankoffsum[1]);

            if (p < sankoffsum[0]) {
                fingerprint[x] = 0;
                leftChild.sankoffmark[0] =
                        (leftChild.sankoffscore[0] <= leftChild.sankoffscore[1] + 1) && (leftChild.sankoffsum[0] != 0);
                leftChild.sankoffmark[1] =
                        (leftChild.sankoffscore[0] >= leftChild.sankoffscore[1] + 1) && (leftChild.sankoffsum[1] != 0);
                rightChild.sankoffmark[0] =
                        (rightChild.sankoffscore[0] <= rightChild.sankoffscore[1] + 1) && (rightChild.sankoffsum[0] != 0);
                rightChild.sankoffmark[1] =
                        (rightChild.sankoffscore[0] >= rightChild.sankoffscore[1] + 1) && (rightChild.sankoffsum[0] != 0);
            } else {
                fingerprint[x] = 1;
                leftChild.sankoffmark[0] =
                        (leftChild.sankoffscore[0] + 1 <= leftChild.sankoffscore[1]) && (leftChild.sankoffsum[0] != 0);
                leftChild.sankoffmark[1] =
                        (leftChild.sankoffscore[0] + 1 >= leftChild.sankoffscore[1]) && (leftChild.sankoffsum[1] != 0);
                rightChild.sankoffmark[0] =
                        (rightChild.sankoffscore[0] + 1 <= rightChild.sankoffscore[1]) && (rightChild.sankoffsum[0] != 0);
                rightChild.sankoffmark[1] =
                        (rightChild.sankoffscore[0] + 1 >= rightChild.sankoffscore[1]) && (rightChild.sankoffsum[0] != 0);
            }

            leftChild.select(x);
            rightChild.select(x);
        }
    }

    public void calculateStatistics() {
        logscore = 0.0;
        if (leftChild != null) {
            leftChild.calculateStatistics();
            rightChild.calculateStatistics();
            logscore += leftChild.logscore + rightChild.logscore;
        }
        if (parent != null) {
            int sum = 0;
            for (int i = 0; i < fingerprint.length; i++) {
                sum += (fingerprint[i] == parent.fingerprint[i] ? 0 : 1);
            }
            // System.out.print(sum + " ");
            for (int i = 2; i <= sum; i++) {
                logscore += Math.log(i);
            }
        }
    }

    // not part of the original core
    public String convertToNewick(int i) {
        if (leftChild == null) {
            double length = 1.0;
            if (evolDist != 0.0) {
                length = evolDist;
            }
            return name + ":" + length;
        } else {
            if ((name == null) || (name == "")) {
                name = "vert" + Integer.toString(i);
            }
            return "(" + leftChild.convertToNewick(2 * i + 1) + " , "
                    + rightChild.convertToNewick(2 * i + 2) + ")"
                    + name + ":" + ((evolDist == 0.0) ? "1.0" : evolDist);
        }
    }

    public boolean hasLeftChild() {
        return leftChild != null;
    }

    public boolean hasRightChild() {
        return rightChild != null;
    }

    public double getAvgLenToLeaves() {
        if (avgLenToLeaves < -0.5) {
            if (!hasLeftChild() && !hasRightChild()) {
                avgLenToLeaves = 0;
            } else if (!hasLeftChild()) {
                System.err.println("TreeNode.java: unbalanced tree!");
                avgLenToLeaves = rightChild.getAvgLenToLeaves();
            } else { // has 2 children
                int lChildren = leftChild.getNumOfLeaves();
                int rChildren = rightChild.getNumOfLeaves();
                avgLenToLeaves = (float) (1.0 / (lChildren + rChildren))
                        * (lChildren * (leftChild.getAvgLenToLeaves() + leftChild.evolDist)
                        + rChildren * (rightChild.getAvgLenToLeaves() + rightChild.evolDist));
            }
        }
        return avgLenToLeaves;
    }

    public int getNumOfLeaves() {
        if (numOfLeaves < 0) {
            if (!hasLeftChild() && !hasRightChild()) {
                numOfLeaves = 1;
            } else if (!hasLeftChild()) {
                System.err.println("TreeNode.java: unbalanced tree!");
                numOfLeaves = rightChild.getNumOfLeaves();
            } else { // has 2 children
                numOfLeaves = leftChild.getNumOfLeaves() + rightChild.getNumOfLeaves();
            }
        }
        return numOfLeaves;
    }

    public double resetAvgLenToLeaves() {
        avgLenToLeaves = -1;
        resetNumOfLeaves();
        return getAvgLenToLeaves();
    }

    public int resetNumOfLeaves() {
        numOfLeaves = -1;
        return getNumOfLeaves();
    }

    public List<Genome> getGenomes(List<Genome> genomes, FingerprintToGenomeConverter fingerprintConverter) {
        if ((leftChild == null) || (rightChild == null)) {
            boolean isContains = false;
            for (Genome genome : genomes) {
                if (genome.getName() == null) {
                    genome.setName("");
                }
                //                LOGGER.warn("genome:\t\t{}", genome.toString());
                //                LOGGER.warn("genome name:\t{}", genome.name);
                //                LOGGER.warn("contain name:\t{}", name);
                if (genome.getName().equals(name)) {
                    isContains = true;
                }
            }
            if (!isContains) {
                throw new RuntimeException("Genomes not contains this leaf: " + name);
            }
            return genomes;
        } else {
            Genome currentGenome = fingerprintConverter.convert(name, fingerprint);
            genomes.add(currentGenome);
            return leftChild.getGenomes(rightChild.getGenomes(genomes, fingerprintConverter), fingerprintConverter);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\n\"fingerprint\":" + Arrays.toString(fingerprint) +
                ",\n\"leftChild\":" + (leftChild != null ? leftChild.toString() : "") +
                ",\n\"rightChild\":" + (rightChild != null ? rightChild.toString() : "") +
                '}';
    }
}

