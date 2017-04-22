package com.genome.comparer.domain.repr;

import java.util.ArrayList;
import java.util.List;

public class ChromosomeRepr {

    private String label;
    private List<BlockRepr> blocks;
    private int size;

    public ChromosomeRepr() {
        blocks = new ArrayList<BlockRepr>();
    }

    public void addBlock(BlockRepr repr) {
        blocks.add(repr);
    }

    public List<BlockRepr> getBlocks() {
        return blocks;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String id) {
        label = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
