package com.gitlab.pedrioko.core.zk.component.chartjs.domain.enums;

public enum ChartType {


    LINE("line"), BAR("bar"), PIE("pie"), DOUGHNUT("doughnut"), POLARAREA("polarArea"), HORIZONALBAR("horizontalBar"), RADAR("radar");

    private final String value;

    private ChartType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
