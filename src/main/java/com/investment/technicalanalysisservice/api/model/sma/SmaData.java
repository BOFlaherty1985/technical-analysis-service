package com.investment.technicalanalysisservice.api.model.sma;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

@Builder
@Getter
@Setter
public final class SmaData {

    private final String date;
    private final String simpleMovingDayAverage;

}
