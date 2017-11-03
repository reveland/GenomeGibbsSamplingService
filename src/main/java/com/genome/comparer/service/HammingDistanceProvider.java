package com.genome.comparer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.genome.comparer.domain.Genome;

@Service
public class HammingDistanceProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(HammingDistanceProvider.class);

    public static double[][] createDistanceMatrix(List<Genome> genomes) {
        int size = genomes.size();
        double[][] distMatrix = new double[size][size];

        fillDistances(genomes, size, distMatrix);
        resetDiagonal(size, distMatrix);

        return distMatrix;
    }

    private static void resetDiagonal(final int size, final double[][] distMatrix) {
        for (int i = 0; i < size; i++) {
            distMatrix[i][i] = 0;
        }
    }

    private static void fillDistances(final List<Genome> genomes, final int size, final double[][] distMatrix) {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                distMatrix[i][j] = distMatrix[j][i] = countDistance(genomes.get(i), genomes.get(j));
            }
        }
    }

    private static int countDistance(final Genome genome1, final Genome genome2) {
        inspectGenomeFingerprintLengths(genome1.fingerprint, genome2.fingerprint);
        return countHammingDistance(genome1.fingerprint, genome2.fingerprint);
    }

    private static int countHammingDistance(final int[] fingerprint1, final int[] fingerprint2) {
        int hamming = 0;
        for (int i = 0; i < fingerprint1.length; i++) {
            if (fingerprint1[i] != fingerprint2[i]) {
                hamming++;
            }
        }
        return hamming;
    }

    private static void inspectGenomeFingerprintLengths(final int[] fingerprint1, final int[] fingerprint2) {
        int length2 = fingerprint2.length;
        int length1 = fingerprint1.length;
        if (length1 != length2) {
            LOGGER.error("Genome fingerprints length are not equal! genome1 length: {}, genome2 length: {}",
                length1, length2);
            throw new RuntimeException("Genome fingerprints length are not equal! genome1 length: "
                + length1 + " and genome2 length: " + length2);
        }
    }

}
