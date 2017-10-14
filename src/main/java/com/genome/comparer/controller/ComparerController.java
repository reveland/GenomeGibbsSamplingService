package com.genome.comparer.controller;

import static com.genome.comparer.service.HammingDistanceProvider.createDistanceMatrix;

import java.io.IOException;
import java.util.ArrayList;
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
import com.genome.comparer.domain.Chromosome;
import com.genome.comparer.domain.RefSquare;
import com.genome.comparer.domain.SquareList;
import com.genome.comparer.domain.data.ComparerData;
import com.genome.comparer.domain.repr.GenomeRepr;
import com.genome.comparer.io.GenomeReader;
import com.genome.comparer.mcmc.GibbsSampler;
import com.genome.comparer.utils.FingerprintToGenomeConverter;
import com.genome.comparer.utils.FittedSquareListMaker;
import com.genome.comparer.utils.GenomeReprMaker;
import com.genome.comparer.utils.ReferenceReprMaker;

@RestController
public class ComparerController {

    private final Logger LOGGER = LoggerFactory.getLogger(GibbsSampler.class);

    @Autowired
    private ReferenceReprMaker referenceReprMaker;
    @Autowired
    private GenomeReprMaker genomeReprMaker;
    @Autowired
    private FittedSquareListMaker fitter;

    private String inputGenomePath = "data/test_genome_data.perm";
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

    @RequestMapping(value = "/genome", method = RequestMethod.GET)
    public ComparerData getGenome(@RequestParam String refGenomeName,
                                  @RequestParam String genomeName1,
                                  @RequestParam String genomeName2) {

        Tree treeFromTask = gibbsSamplerTask.getTree();

        // fingerprint converter could be field? the adjacencies doesn't change
        FingerprintToGenomeConverter fingerprintConverter =
                new FingerprintToGenomeConverter(treeFromTask.adjacencies);

        LOGGER.info("\ntreeFromTask adjs: {}", treeFromTask.adjacencies);

        List<Genome> genomes = treeFromTask.getGenomes(fingerprintConverter);
        Map<String, Genome> genomesMap = genomes.stream()
                .collect(Collectors.toMap(Genome::getName, genome -> genome));

        Genome genome1 = genomesMap.get(genomeName1);
        Genome genome2 = genomesMap.get(genomeName2);
        Genome referenceGenome = genomesMap.get(refGenomeName);

        LOGGER.info("\nGet Comparer Data: \nReference: {}\nGenome1: {}\nGenome2: {}",
                referenceGenome, genome1, genome2);

        List<RefSquare> refSquares = referenceReprMaker.make(referenceGenome);

        LOGGER.info("\nReference Squares: {}", refSquares);

        List<Chromosome> originalChromosomesR = referenceGenome.original;
        List<Chromosome> originalChromosomes1 = genome1.original;
        List<Chromosome> originalChromosomes2 = genome2.original;

        List<SquareList> listOfSquareListsR = fitter.fit(originalChromosomesR, refSquares);
        List<SquareList> listOfSquareLists1 = fitter.fit(originalChromosomes1, refSquares);
        List<SquareList> listOfSquareLists2 = fitter.fit(originalChromosomes2, refSquares);

        LOGGER.info("\nFitted Square Lists: \nReference: {}\nGenome1: {}\nGenome2: {}",
                listOfSquareListsR, listOfSquareLists1, listOfSquareLists2);

        ComparerData comparerData = genomeReprMaker.makeComparerData(listOfSquareListsR, listOfSquareLists1, listOfSquareLists2);

        LOGGER.info("\nComparer Data: \n{}", comparerData);

        return comparerData;
    }
}
