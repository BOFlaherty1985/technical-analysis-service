package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.alphavantageapi.model.sma.MetaData;
import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.alphavantageapi.model.sma.SmaData;
import com.investment.alphavantageapi.model.sma.TechnicalAnalysis;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import org.junit.Test;
import problemdetail.ProblemDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SimpleMovingDayAverageCurrentDayValidationRuleTest {

    private SimpleMovingDayAverageCurrentDayValidationRule rule
            = new SimpleMovingDayAverageCurrentDayValidationRule();


    @Test
    public void shouldCheckThatCurrentDayDataIsAvailable() {
        // given
        List<SmaData> smaDataList = List.of(SmaData.builder()
                .date(LocalDate.now().toString())
                .simpleMovingDayAverage("190.00")
                .build());

        TechnicalAnalysis technicalAnalysis = TechnicalAnalysis.builder()
                .simpleMovingDayAverages(smaDataList)
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().symbol("XYZ").build())
                .technicalAnalysis(technicalAnalysis)
                .build();

        SimpleMovingDayAverageCalculatorRequest request =
                SimpleMovingDayAverageCalculatorRequest.builder()
                        .stockPrice("200.00")
                        .data(data)
                        .build();

        // when
        Optional<ProblemDetail> result = rule.validate(request);

        // then
        assertThat(result.isEmpty());
    }

}
