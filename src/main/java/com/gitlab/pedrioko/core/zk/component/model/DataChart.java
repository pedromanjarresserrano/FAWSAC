package com.gitlab.pedrioko.core.zk.component.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public @Data
class DataChart {

    private String label;

    private Double value;

    private String backgroundColor;

    private String borderColor;

    public DataChart() {
    }

    public DataChart(String label, Double value, String backgroundColor, String borderColor) {
        this.label = label;
        this.value = value;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public DataChart(String label, Double value) {
        this.label = label;
        this.value = value;
    }
}
