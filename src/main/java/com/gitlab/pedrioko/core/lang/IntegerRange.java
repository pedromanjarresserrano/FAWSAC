package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.api.RangeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class IntegerRange implements RangeValue {
    private Integer inicio;
    private Integer fin;
}

