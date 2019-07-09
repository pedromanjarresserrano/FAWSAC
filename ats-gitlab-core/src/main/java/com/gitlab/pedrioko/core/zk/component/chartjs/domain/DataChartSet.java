package com.gitlab.pedrioko.core.zk.component.chartjs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public
class DataChartSet {
    private String label;
    private String backgroundColor;
    private String borderColor;
    private Integer borderWidth;
    private List<String> data;

}
