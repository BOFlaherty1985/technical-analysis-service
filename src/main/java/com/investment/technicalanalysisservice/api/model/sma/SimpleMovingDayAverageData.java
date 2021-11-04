package com.investment.technicalanalysisservice.api.model.sma;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Builder
@Getter
@Setter
public class SimpleMovingDayAverageData {

    @SerializedName("Meta Data")
    private MetaData metaData;

    @SerializedName("Technical Analysis: SMA")
    private TechnicalAnalysis technicalAnalysis;

}
