package com.genome.comparer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SyntenyReCounter {

    private final static Logger LOGGER = LoggerFactory.getLogger(SyntenyReCounter.class);

    public static int[] reversePair(int[] adjacency) {
        int[] result = new int[2];

        if (adjacency[0] % 2 == 0) {
            result[0] = adjacency[0] / 2;
        } else {
            result[0] = (adjacency[0] + 1) / (-2);
        }

        if (adjacency[1] % 2 == 0) {
            result[1] = adjacency[1] / (-2);
        } else {
            result[1] = (adjacency[1] + 1) / 2;
        }

        return result;
    }

    public static List<List<Integer>> makeChains(List<int[]> originalAdjacencies) {

        List<int[]> origPairsInput = originalAdjacencies.stream()
            .map(adjacency -> new int[]{adjacency[0], adjacency[1]})
            .collect(Collectors.toList());

        List<int[]> originalPairs = new ArrayList<>(origPairsInput);
        List<List<int[]>> result = new ArrayList<>();

        while (originalPairs.size() > 0) {
            List<int[]> currentChain = getCurrentChain(originalPairs);
            result.add(currentChain);
        }
        List<List<Integer>> resultWithIntegers = new ArrayList<>();
        for (List<int[]> origPairs : result) {
            List<Integer> chromosome = getOriginalChromosome(origPairs);
            resultWithIntegers.add(chromosome);
        }
        return resultWithIntegers;
    }

    private static List<int[]> getCurrentChain(final List<int[]> originalPairs) {
        List<int[]> currentChain = new ArrayList<>();

        currentChain.add(originalPairs.get(0));
        int prev_end = originalPairs.get(0)[1];
        originalPairs.remove(0);

        boolean found = true;
        while (found) {
            found = false;
            for (int i = 0; i < originalPairs.size() && !found; ++i) {
                if (prev_end == originalPairs.get(i)[0]) {
                    currentChain.add(originalPairs.get(i));
                    prev_end = originalPairs.get(i)[1];
                    originalPairs.remove(i);
                    found = true;
                }
            }
            if (!found) {
                for (int i = 0; i < originalPairs.size() && !found; ++i) {
                    if (prev_end == (-1) * originalPairs.get(i)[1]) {
                        int[] pair = new int[]{originalPairs.get(i)[1] * (-1),
                            originalPairs.get(i)[0] * (-1)};
                        currentChain.add(pair);
                        prev_end = originalPairs.get(i)[0] * (-1);
                        originalPairs.remove(i);
                        found = true;
                    }
                }
            }
        }

        int prev_start = currentChain.get(0)[0];
        found = true;
        while (found) {
            found = false;
            for (int i = 0; i < originalPairs.size() && !found; ++i) {
                if (prev_start == originalPairs.get(i)[1]) {
                    currentChain.add(0, originalPairs.get(i));
                    prev_start = originalPairs.get(i)[0];
                    originalPairs.remove(i);
                    found = true;
                }
            }
            if (!found) {
                for (int i = 0; i < originalPairs.size() && !found; ++i) {
                    if (prev_start == (-1) * originalPairs.get(i)[0]) {
                        int[] pair = new int[]{originalPairs.get(i)[1] * (-1),
                            originalPairs.get(i)[0] * (-1)};
                        currentChain.add(0, pair);
                        prev_start = originalPairs.get(i)[1] * (-1);
                        originalPairs.remove(i);
                        found = true;
                    }
                }
            }
        }
        return currentChain;
    }

    private static List<Integer> getOriginalChromosome(List<int[]> pairs) {
        List<Integer> chromosome = new ArrayList<>();
        for (int[] pair : pairs) {
            chromosome.add(pair[0]);
        }
        return chromosome;
    }

}
