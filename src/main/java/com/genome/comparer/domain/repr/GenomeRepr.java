package com.genome.comparer.domain.repr;

import java.util.ArrayList;
import java.util.List;

public class GenomeRepr {

    private String name;
    private List<ChromosomeRepr> chromosomes;

    public GenomeRepr() {
        chromosomes = new ArrayList<>();
    }

    public void addChromosome(ChromosomeRepr chrRepr) {
        chromosomes.add(chrRepr);
    }

    public List<ChromosomeRepr> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(final List<ChromosomeRepr> chromosomes) {
        this.chromosomes = chromosomes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
