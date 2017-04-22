package com.genome.comparer.domain.data;

public class BlockData {

    private int id;
    private String referenceChromosomeLabel;
    private int size;

    public BlockData(final int id, final String referenceChromosomeLabel, final int size) {
        this.id = id;
        this.referenceChromosomeLabel = referenceChromosomeLabel;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getReferenceChromosomeLabel() {
        return referenceChromosomeLabel;
    }

    public void setReferenceChromosomeLabel(final String referenceChromosomeLabel) {
        this.referenceChromosomeLabel = referenceChromosomeLabel;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }
}
