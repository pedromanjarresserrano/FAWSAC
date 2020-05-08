package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.api.RangeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class FileSizeRange implements RangeValue {
    private BigDecimal inicio;
    private BigDecimal fin;
}

