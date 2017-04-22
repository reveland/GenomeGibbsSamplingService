package com.genome.comparer.domain.data;

import java.util.List;

public class ComparerData {

    private List<BlockData> blocks;
    private List<GenomeData> genomes;

    public ComparerData(final List<BlockData> blocks, final List<GenomeData> genomes) {
        this.blocks = blocks;
        this.genomes = genomes;
    }

    public List<BlockData> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<BlockData> blocks) {
        this.blocks = blocks;
    }

    public List<GenomeData> getGenomes() {
        return genomes;
    }

    public void setGenomes(final List<GenomeData> genomes) {
        this.genomes = genomes;
    }
}
