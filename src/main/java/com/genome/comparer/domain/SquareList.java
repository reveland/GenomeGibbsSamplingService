package com.genome.comparer.domain;

import java.util.List;

public class SquareList {
    private String label;
    private List<Square> squares;

    public SquareList(String label, List<Square> squares) {
        this.label = label;
        this.squares = squares;
    }

    public String getLabel() {
        return label;
    }

    public List<Square> getSquares() {
        return squares;
    }

    @Override
    public String toString() {
        return "\nSquareList{" +
            "label='" + label + '\'' +
            ", squares=" + squares +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SquareList that = (SquareList) o;

        if (label != null ? !label.equals(that.label) : that.label != null)
            return false;
        if (squares != null ? !squares.equals(that.squares) : that.squares != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (squares != null ? squares.hashCode() : 0);
        return result;
    }
}
