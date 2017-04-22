package com.genome.comparer.domain.data;

import java.util.List;

public class ChromosomeData {

    private String label;
    private List<ChrBlockData> blocks;

    public ChromosomeData(final String label, final List<ChrBlockData> blocks) {
        this.label = label;
        this.blocks = blocks;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public List<ChrBlockData> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<ChrBlockData> blocks) {
        this.blocks = blocks;
    }
}
