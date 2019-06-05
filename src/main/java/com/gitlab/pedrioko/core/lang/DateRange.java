package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.api.RangeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class DateRange implements RangeValue {
    private Date inicio;
    private Date fin;
}

