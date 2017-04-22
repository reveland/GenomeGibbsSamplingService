package com.genome.comparer.controller;

import static com.genome.comparer.service.HammingDistanceProvider.createDistanceMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genome.comparer.algorithm.Genome;
import com.genome.comparer.algorithm.PooledAdjacencies;
import com.genome.comparer.algorithm.Tree;
import com.genome.comparer.datastructures.UPGMATreeMaker;
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

    private String inputGenomePath = "data/destilled.txt";
    private GibbsSampler gibbsSamplerTask;
    private Thread gibbsSamplerThread;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start() throws IOException {

        List<Genome> genomes = GenomeReader.read(inputGenomePath);

        PooledAdjacencies pooledAdjacencies = new PooledAdjacencies(genomes);
        for (Genome genome : genomes) {
            genome.fingerprint = pooledAdjacencies.fingerprint(genome);
        }

        double[][] matrix = createDistanceMatrix(genomes);

        UPGMATreeMaker upgma = new UPGMATreeMaker();
        Tree tree = upgma.makeTree(matrix, genomes, pooledAdjacencies);

        tree.genomes = genomes;
        tree.adjacencies = pooledAdjacencies;
        gibbsSamplerTask = new GibbsSampler(tree);
        gibbsSamplerThread = new Thread(gibbsSamplerTask);
        gibbsSamplerThread.start();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop() {
        if (gibbsSamplerThread != null) {
            gibbsSamplerThread.stop();
            gibbsSamplerThread = null;
        } else {
            gibbsSamplerTask = null;
        }
    }

    @RequestMapping(value = "/genome", method = RequestMethod.GET)
    public ComparerData getGenome(@RequestParam String refGenomeName, @RequestParam String genomeName1,
        @RequestParam String genomeName2) {

        Tree treeFromTask = gibbsSamplerTask.getTree();
        FingerprintToGenomeConverter fingerprintConverter = new FingerprintToGenomeConverter(treeFromTask.adjacencies);
        List<Genome> genomes = treeFromTask.getGenomes(fingerprintConverter);
        Map<String, Genome> genomesMap = genomes.stream()
            .collect(Collectors.toMap(genome -> genome.getName(), genome -> genome));

        Genome genome1 = genomesMap.get(genomeName1);
        Genome genome2 = genomesMap.get(genomeName2);
        Genome referenceGenome = genomesMap.get(refGenomeName);

        List<RefSquare> refSquares = referenceReprMaker.make(referenceGenome);

        List<Chromosome> originalChromosomesR = referenceGenome.original;
        List<Chromosome> originalChromosomes1 = genome1.original;
        List<Chromosome> originalChromosomes2 = genome2.original;

        List<SquareList> listOfSquareListsR = fitter.fit(originalChromosomesR, refSquares);
        List<SquareList> listOfSquareLists1 = fitter.fit(originalChromosomes1, refSquares);
        List<SquareList> listOfSquareLists2 = fitter.fit(originalChromosomes2, refSquares);

        return genomeReprMaker.makeComparerData(listOfSquareListsR, listOfSquareLists1, listOfSquareLists2);
    }

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public GenomeRepr sample(@RequestParam String refGenomeName, @RequestParam String genomeName) {

        Tree treeFromTask = gibbsSamplerTask.getTree();
        LOGGER.info(String.valueOf(treeFromTask.root.logscore));

        ReferenceReprMaker referenceReprMaker = new ReferenceReprMaker();
        GenomeReprMaker genomeReprMaker = new GenomeReprMaker();
        FittedSquareListMaker fitter = new FittedSquareListMaker();

        List<SquareList> squareLists;
        List<RefSquare> refSquares = new ArrayList<>();
        List<Chromosome> chromosomes = new ArrayList<>();

        FingerprintToGenomeConverter fingerprintConverter = new FingerprintToGenomeConverter(treeFromTask.adjacencies);
        List<Genome> genomes = treeFromTask.getGenomes(fingerprintConverter);

        for (Genome genome : genomes) {
            if (genome.getName().equals(genomeName)) {
                chromosomes = genome.original;
            }
            if (genome.getName().equals(refGenomeName)) {
                refSquares = referenceReprMaker.make(genome);
            }
        }
        squareLists = fitter.fit(chromosomes, refSquares);
        return genomeReprMaker.make(squareLists, genomeName);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public String getTree() {
        Tree treeFromTask = gibbsSamplerTask.getTree();
        return treeFromTask.root.convertToNewick(0);
    }
}
