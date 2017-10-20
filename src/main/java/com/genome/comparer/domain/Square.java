package com.genome.comparer.domain;

public class Square {

    private String label;
    private boolean inverted;
    private int id;

    public Square(String label, boolean inverted) {
        this.label = label;
        this.inverted = inverted;
    }

    public Square(final String label, final boolean inverted, final int id) {
        this.label = label;
        this.inverted = inverted;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isInverted() {
        return inverted;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ", \"label\":\"" + label + "\", \"inverted\":" + inverted + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Square square = (Square) o;

        if (inverted != square.inverted)
            return false;
        if (label != null ? !label.equals(square.label) : square.label != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (inverted ? 1 : 0);
        return result;
    }
}
