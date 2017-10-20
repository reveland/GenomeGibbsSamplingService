package com.genome.comparer.domain;

public class RefSquare {
    private int adjacency;
    private String label;
    private int id;

    public RefSquare(int adjacency, String label) {
        this.adjacency = adjacency;
        this.label = label;
    }

    public RefSquare(int adjacency, String label, int id) {
        this.adjacency = adjacency;
        this.label = label;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getAdjacency() {
        return adjacency;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":%d, \"adjacency\":%d, \"label\":\"%s\"}", id, adjacency, label);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RefSquare refSquare = (RefSquare) o;

        if (adjacency != refSquare.adjacency)
            return false;
        if (label != null ? !label.equals(refSquare.label) : refSquare.label != null)
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
