package com.gitlab.pedrioko.core.zk.component.chartjs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public
class DataChart {
    List<String> labels;
    List<DataChartSet> datasets;
}
