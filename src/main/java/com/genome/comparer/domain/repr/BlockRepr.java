package com.genome.comparer.domain.repr;

public class BlockRepr {

    private int size;
    private String refChrLabel;
    private boolean inverted;
    private int id;

    public BlockRepr() {
    }

    public BlockRepr(int id, String refChrLabel, int size, boolean inverted) {
        this.size = size;
        this.refChrLabel = refChrLabel;
        this.inverted = inverted;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getRefChrLabel() {
        return refChrLabel;
    }

    public void setRefChrLabel(String id) {
        refChrLabel = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(final boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public String toString() {
        return String.format("Block{id:%d, ref:%s, size:%d, invert:%b}\n", id, refChrLabel, size, inverted);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BlockRepr blockRepr = (BlockRepr) o;

        if (size != blockRepr.size)
            return false;
        if (inverted != blockRepr.inverted)
            return false;
        if (id != blockRepr.id)
            return false;
        if (refChrLabel != null ? !refChrLabel.equals(blockRepr.refChrLabel) : blockRepr.refChrLabel != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (refChrLabel != null ? refChrLabel.hashCode() : 0);
        result = 31 * result + (inverted ? 1 : 0);
        result = 31 * result + id;
        return result;
    }
}
