package com.investment.technicalanalysisservice.sma.web.validator;

import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import lombok.*;

import java.util.function.Predicate;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SimpleMovingDayAverageCalculatorRequest {

    private String stockPrice;
    private SimpleMovingDayAverageData data;

    public boolean isSimpleMovingDayAverageDataMissing() {
        return data == null;
    }

    public static Predicate<String> stockPriceNullOrEmpty = stockPrice ->  stockPrice == null || stockPrice.isEmpty();

}
