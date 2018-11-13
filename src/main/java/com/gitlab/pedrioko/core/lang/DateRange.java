package com.gitlab.pedrioko.core.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public @Data
class DateRange {
    private Date inicio;
    private Date fin;
}

