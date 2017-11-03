package com.genome.comparer.controller;

import static com.genome.comparer.service.HammingDistanceProvider.createDistanceMatrix;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.genome.comparer.tree.TreeMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genome.comparer.core.Genome;
import com.genome.comparer.core.PooledAdjacencies;
import com.genome.comparer.core.Tree;
import com.genome.comparer.tree.UPGMATreeMaker;
import com.genome.comparer.domain.RefSquare;
import com.genome.comparer.io.GenomeReader;
import com.genome.comparer.mcmc.GibbsSampler;
import com.genome.comparer.utils.FingerprintToGenomeConverter;
import com.genome.comparer.utils.ReferenceReprMaker;

@RestController
public class ComparerController {

    private final Logger LOGGER = LoggerFactory.getLogger(GibbsSampler.class);

    @Autowired
    private ReferenceReprMaker referenceReprMaker;

    private static final String inputGenomePath = "data/destilled.txt";
    private GibbsSampler gibbsSamplerTask;
    private Thread gibbsSamplerThread;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start() throws IOException {
        LOGGER.info("Started");

        List<Genome> genomes = GenomeReader.read(inputGenomePath);

        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(genomes);
        for (Genome genome : genomes) {
            genome.fingerprint = pooledAdjacencies.fingerprint(genome);
        }

        double[][] matrix = createDistanceMatrix(genomes);

        TreeMaker treeMaker = new UPGMATreeMaker();
        Tree tree = treeMaker.makeTree(matrix, genomes, pooledAdjacencies);

        tree.genomes = genomes;
        tree.adjacencies = pooledAdjacencies;
        gibbsSamplerTask = new GibbsSampler(tree);
        gibbsSamplerThread = new Thread(gibbsSamplerTask);
        gibbsSamplerThread.start();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop() {
        LOGGER.info("Stopped");
        if (gibbsSamplerThread != null) {
            gibbsSamplerThread.stop();
            gibbsSamplerThread = null;
        } else {
            gibbsSamplerTask = null;
        }
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public String getTree() {
        Tree treeFromTask = gibbsSamplerTask.getTree();
        String newick = treeFromTask.root.convertToNewick(0);
        LOGGER.info("Get Tree: \nObject: {}\nNewick: {}", treeFromTask, newick);
        return newick;
    }

    @RequestMapping(value = "/getSquares", method = RequestMethod.GET)
    public List<RefSquare> getSquares(@RequestParam String genomeName) {
        Tree treeFromTask = gibbsSamplerTask.getTree();

        FingerprintToGenomeConverter fingerprintConverter = new FingerprintToGenomeConverter(treeFromTask.adjacencies);

        List<Genome> genomes = treeFromTask.getGenomes(fingerprintConverter);
        Map<String, Genome> genomesMap = genomes.stream().collect(Collectors.toMap(Genome::getName, genome -> genome));

        Genome genome = genomesMap.get(genomeName);

        LOGGER.info("{}:\n{}", genomeName, genome);

        return referenceReprMaker.make(genome.original);
    }
}
