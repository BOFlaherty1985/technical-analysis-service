package com.investment.technicalanalysisservice.api.model.sma;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TechnicalAnalysis {

    public List<SmaData> simpleMovingDayAverages;

}