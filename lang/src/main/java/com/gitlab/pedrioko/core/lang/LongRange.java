package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.api.RangeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class LongRange implements RangeValue {
    private Long inicio;
    private Long fin;
}

