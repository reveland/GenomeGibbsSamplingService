package com.genome.comparer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.genome.comparer.domain.Square;
import com.genome.comparer.domain.SquareList;
import com.genome.comparer.domain.data.BlockData;
import com.genome.comparer.domain.data.ChrBlockData;
import com.genome.comparer.domain.data.ChromosomeData;
import com.genome.comparer.domain.data.ComparerData;
import com.genome.comparer.domain.data.GenomeData;
import com.genome.comparer.domain.repr.BlockRepr;
import com.genome.comparer.domain.repr.ChromosomeRepr;
import com.genome.comparer.domain.repr.GenomeRepr;

@Service
public class GenomeReprMaker {

    public ComparerData makeComparerData(
        final List<SquareList> listOfSquareListsR,
        final List<SquareList> listOfSquareLists1,
        final List<SquareList> listOfSquareLists2) {

        List<BlockData> blocks = listOfSquareListsR.stream()
            .flatMap(squareList -> squareList.getSquares().stream())
            .map(square -> new BlockData(square.getId(), square.getLabel(), 1))
            .sorted(Comparator.comparingInt(BlockData::getId))
            .collect(Collectors.toList());

        return new ComparerData(blocks, Arrays.asList(squaresToGenome(listOfSquareListsR, "referenceGenom"),
            squaresToGenome(listOfSquareLists1, "genomeName1"), squaresToGenome(listOfSquareLists2, "genomeName2")));
    }

    private GenomeData squaresToGenome(final List<SquareList> listOfSquareLists, final String genomeName) {
        return new GenomeData(genomeName, squaresToChromosomes(listOfSquareLists));
    }

    private List<ChromosomeData> squaresToChromosomes(final List<SquareList> listOfSquareLists) {
        return listOfSquareLists.stream()
            .map(squareList -> new ChromosomeData(squareList.getLabel(), squaresToBlocks(squareList.getSquares())))
            .sorted((o1, o2) -> compareLabels(o1.getLabel(), o2.getLabel()))
            .collect(Collectors.toList());
    }

    private int compareLabels(String o1, String o2) {
        return isNumeric(o1) && isNumeric(o2)
            ? Integer.parseInt(o1) - Integer.parseInt(o2)
            : isNumeric(o1) ? -1 : 1;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private List<ChrBlockData> squaresToBlocks(final List<Square> squares) {
        return squares.stream()
            .map(square -> new ChrBlockData(square.getId(), square.isInverted()))
            .collect(Collectors.toList());
    }

    public GenomeRepr make(List<SquareList> squareLists, String genomeName) {
        GenomeRepr genomeRepr = new GenomeRepr();
        genomeRepr.setName(genomeName);
        List<ChromosomeRepr> chromosomeReprs = getChromosomeReprs(squareLists);
        for (ChromosomeRepr chromosomeRepr : chromosomeReprs) {
            genomeRepr.addChromosome(chromosomeRepr);
        }
        return genomeRepr;
    }

    private List<ChromosomeRepr> getChromosomeReprs(List<SquareList> squareLists) {
        List<ChromosomeRepr> chromosomeReprs = new ArrayList<>();
        for (SquareList squareList : squareLists) {
            ChromosomeRepr chromosomeRepr = new ChromosomeRepr();
            chromosomeRepr.setLabel(squareList.getLabel());
            List<BlockRepr> blockReprs = getBlockReprsWithoutMerging(squareList.getSquares());
            int size = 0;
            for (BlockRepr blockRepr : blockReprs) {
                size += blockRepr.getSize();
                chromosomeRepr.addBlock(blockRepr);
            }
            chromosomeRepr.setSize(size);
            chromosomeReprs.add(chromosomeRepr);
        }
        return chromosomeReprs;
    }

    private List<BlockRepr> getBlockReprsWithoutMerging(List<Square> squares) {
        return squares.stream()
            .map(square -> new BlockRepr(square.getId(), square.getLabel(), 1, square.isInverted()))
            .collect(Collectors.toList());
    }

    public List<BlockRepr> getBlockReprs(List<Square> squares) {
        List<BlockRepr> blockReprs = new ArrayList<>();
        BlockRepr blockRepr = new BlockRepr();
        boolean isFirst = true;
        String prevLabel = "0";
        int size = 0;
        int id = 0;
        boolean lastInvertState = false;
        String prevDirection = "none";
        for (Square square : squares) {
            String direction = getDirection(square);
            if (isFirst) {
                isFirst = false;
            } else {
                if (!square.getLabel().equals(prevLabel) || !direction.equals(prevDirection)) {
                    blockRepr.setRefChrLabel(prevLabel);
                    blockRepr.setSize(size);
                    blockRepr.setInverted(prevDirection.equals("up") ? true : false);
                    blockRepr.setId(id);
                    id++;
                    blockReprs.add(blockRepr);
                    blockRepr = new BlockRepr();
                    size = 0;
                }
            }
            prevLabel = square.getLabel();
            lastInvertState = square.isInverted();
            prevDirection = direction;
            size++;
        }
        blockRepr.setRefChrLabel(prevLabel);
        blockRepr.setSize(size);
        blockRepr.setInverted(lastInvertState);
        blockRepr.setId(id);
        blockReprs.add(blockRepr);
        return blockReprs;
    }

    private String getDirection(Square square) {
        String direction = "none";
        if (square.isInverted()) {
            direction = "up";
        } else if (!square.isInverted()) {
            direction = "down";
        } else {
            direction = "error";
        }
        return direction;
    }
}
