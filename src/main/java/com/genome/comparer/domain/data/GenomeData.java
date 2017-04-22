package com.genome.comparer.domain.data;

import java.util.List;

public class GenomeData {
    private String name;
    private List<ChromosomeData> chromosomes;

    public GenomeData(final String name, final List<ChromosomeData> chromosomes) {
        this.name = name;
        this.chromosomes = chromosomes;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<ChromosomeData> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(final List<ChromosomeData> chromosomes) {
        this.chromosomes = chromosomes;
    }
}
