package com.genome.comparer.domain.data;

public class ChrBlockData {
    private int id;
    private boolean inverted;

    public ChrBlockData(final int id, final boolean inverted) {
        this.id = id;
        this.inverted = inverted;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(final boolean inverted) {
        this.inverted = inverted;
    }
}
