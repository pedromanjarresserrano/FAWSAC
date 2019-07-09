package com.gitlab.pedrioko.domain.enumdomain;

public enum TipoChart {


    LINE("line"), BAR("bar"), PIE("pie"), DOUGHNUT("doughnut"), POLARAREA("polarArea"), HORIZONALBAR("horizontalBar"), RADAR("radar");

    private final String value;

    private TipoChart(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
