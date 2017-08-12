package com.genome.comparer.mcmc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genome.comparer.core.Tree;
import com.genome.comparer.io.VertebrateTreeConstructor;

public class GibbsSampler implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(GibbsSampler.class);

    private Tree tree;
    private int steps = 10000;

    public GibbsSampler(Tree tree) {
        this.tree = tree;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < steps; i++) {
                tree.gibbsSampling();
            }
            tree.root.calculateStatistics();
            LOGGER.debug(String.valueOf(tree.root.logscore));
        }
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public static void main(String[] args) throws IOException {
        long millis = System.currentTimeMillis();

        int burnIn = 0;
        int numberOfSamples = 100;
        int steps = 10000;
        String inputPath = "./data/destilled.txt";
        if (args.length > 0)
            burnIn = Integer.parseInt(args[0]);
        if (args.length > 1)
            numberOfSamples = Integer.parseInt(args[1]);
        if (args.length > 2)
            steps = Integer.parseInt(args[2]);
        if (args.length > 3)
            inputPath = args[3];

        GibbsSampler gs = new GibbsSampler(VertebrateTreeConstructor.treeBuilder(inputPath));
        gs.doMCMC(burnIn, numberOfSamples, steps);

        LOGGER.debug("{}", System.currentTimeMillis() - millis);
    }

    private void doMCMC(int burnIn, int numberOfSamples, int steps) {
        for (int i = 0; i < burnIn; i++) {
            tree.gibbsSampling();
        }
        for (int i = 0; i < numberOfSamples; i++) {
            for (int j = 0; j < steps; j++) {
                tree.gibbsSampling();
            }
            tree.root.calculateStatistics();
            LOGGER.debug(String.valueOf(tree.root.logscore));
        }
    }
}
