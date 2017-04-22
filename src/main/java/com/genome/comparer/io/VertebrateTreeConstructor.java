package com.genome.comparer.io;

import java.io.IOException;
import java.util.ArrayList;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.algorithm.Tree;
import com.genome.comparer.algorithm.TreeNode;

public class VertebrateTreeConstructor {

    public static Tree treeBuilder(String inputPath) throws IOException {
        ArrayList<Genome> genomes = GenomeReader.read(inputPath);
        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(genomes);

        Tree tree = new Tree();
        tree.adjacencies = pooledAdjacencies;

        TreeNode root = new TreeNode();
        tree.root = root;
        root.owner = tree;
        root.name = "root";

        TreeNode galGal = new TreeNode();
        galGal.owner = tree;
        galGal.parent = root;
        root.leftChild = galGal;
        galGal.name = "galGal";
        galGal.fingerprint = pooledAdjacencies.fingerprint(genomes.get(7));
        System.out.println("crosscheck: this is indeed galGal: " + genomes.get(7).getName());

        TreeNode mammalian = new TreeNode();
        mammalian.owner = tree;
        mammalian.parent = root;
        root.rightChild = mammalian;
        mammalian.name = "mammalian";

        TreeNode monDom = new TreeNode();
        monDom.owner = tree;
        monDom.parent = mammalian;
        mammalian.leftChild = monDom;
        monDom.name = "monDom";
        monDom.fingerprint = pooledAdjacencies.fingerprint(genomes.get(6));
        System.out.println("crosscheck: this is indeed monDom: " + genomes.get(6).getName());

        TreeNode placentae = new TreeNode();
        placentae.owner = tree;
        placentae.parent = mammalian;
        mammalian.rightChild = placentae;
        placentae.name = "placentae";

        TreeNode boreo = new TreeNode();
        boreo.owner = tree;
        boreo.parent = placentae;
        placentae.leftChild = boreo;
        boreo.name = "boreo";

        TreeNode bosTau = new TreeNode();
        bosTau.owner = tree;
        bosTau.parent = boreo;
        boreo.leftChild = bosTau;
        bosTau.name = "bosTau";
        bosTau.fingerprint = pooledAdjacencies.fingerprint(genomes.get(5));
        System.out.println("crosscheck: this is indeed bosTau: " + genomes.get(5).getName());

        TreeNode canFam = new TreeNode();
        canFam.owner = tree;
        canFam.parent = boreo;
        boreo.rightChild = canFam;
        canFam.name = "canFam";
        canFam.fingerprint = pooledAdjacencies.fingerprint(genomes.get(4));
        System.out.println("crosscheck: this is indeed canFam: " + genomes.get(4).getName());

        TreeNode x = new TreeNode();
        x.owner = tree;
        x.parent = placentae;
        placentae.rightChild = x;
        x.name = "x";

        TreeNode rodents = new TreeNode();
        rodents.owner = tree;
        rodents.parent = x;
        x.leftChild = rodents;
        rodents.name = "rodents";

        TreeNode mm = new TreeNode();
        mm.owner = tree;
        mm.parent = rodents;
        rodents.leftChild = mm;
        mm.name = "mm";
        mm.fingerprint = pooledAdjacencies.fingerprint(genomes.get(2));
        System.out.println("crosscheck: this is indeed mm: " + genomes.get(2).getName());

        TreeNode rn = new TreeNode();
        rn.owner = tree;
        rn.parent = rodents;
        rodents.rightChild = rn;
        rn.name = "rn";
        rn.fingerprint = pooledAdjacencies.fingerprint(genomes.get(3));
        System.out.println("crosscheck: this is indeed rn: " + genomes.get(3).getName());

        TreeNode primates = new TreeNode();
        primates.owner = tree;
        primates.parent = x;
        x.rightChild = primates;
        primates.name = "primates";

        TreeNode hg1 = new TreeNode();
        hg1.owner = tree;
        hg1.parent = primates;
        primates.leftChild = hg1;
        hg1.name = "hg1";
        hg1.fingerprint = pooledAdjacencies.fingerprint(genomes.get(0));
        System.out.println("crosscheck: this is indeed hg1: " + genomes.get(0).getName());

        TreeNode rheMac = new TreeNode();
        rheMac.owner = tree;
        rheMac.parent = primates;
        primates.rightChild = rheMac;
        rheMac.name = "rheMac";
        rheMac.fingerprint = pooledAdjacencies.fingerprint(genomes.get(1));
        System.out.println("crosscheck: this is indeed rheMac: " + genomes.get(1).getName());

        tree.crosscheck();

        tree.fitch();

        return tree;
    }

    public static void main(String[] args) throws IOException {
        String inputPath = "./data/destilled.txt";

        Tree tree = treeBuilder(inputPath);
        System.out.println("number of pooled deprecatedAdjacencies: " + tree.adjacencies.getAdjacencies().size()
            + ", parsomony score: " + tree.parsimonyScore());
        for (int i = 0; i < 0; i++) {
            tree.gibbsSampling();
        }
    }
}
