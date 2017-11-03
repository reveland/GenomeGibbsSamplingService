package com.genome.comparer.domain;

public class Square {

    private int adjacency;
    private String label;

    public Square(int adjacency, String label) {
        this.adjacency = adjacency;
        this.label = label;
    }

    public int getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(int adjacency) {
        this.adjacency = adjacency;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("{\"adjacency\":%d, \"label\":\"%s\"}", adjacency, label);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Square square = (Square) o;

        if (adjacency != square.adjacency)
            return false;
        if (label != null ? !label.equals(square.label) : square.label != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = adjacency;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
