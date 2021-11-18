package com.investment.technicalanalysisservice.sma.web.validation;

import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.alphavantageapi.model.sma.SmaData;
import com.investment.alphavantageapi.model.sma.TechnicalAnalysis;
import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageCurrentDayValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageDataValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.StockPriceValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.ValidationRule;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMovingDayAverageCalculationValidatorTest {

    @InjectMocks
    private SimpleMovingDayAverageCalculationValidator validator;

    @Spy
    private List<ValidationRule> validationRules = new ArrayList<>();

    @Mock
    private StockPriceValidationRule stockPriceValidationRule;

    @Mock
    private SimpleMovingDayAverageDataValidationRule dataValidationRule;

    @Mock
    private SimpleMovingDayAverageCurrentDayValidationRule currentDayValidationRule;

    @Before
    public void beforeAll() {
        validationRules.add(stockPriceValidationRule);
        validationRules.add(dataValidationRule);
        validationRules.add(currentDayValidationRule);
    }

    @Test
    public void shouldValidateRequestForValidStockPrice() {
        // given
        String stockPrice = getRandomValueOfEmptyOrNull();
        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice(stockPrice)
                .build();

        String title = "A stock price must be supplied in order to calculate the moving day average indicator";
        ProblemDetail problemDetail = ProblemDetailBuilder
                .problemDetailBuilder()
                .title(title)
                .build();

        when(stockPriceValidationRule.validate(request)).thenReturn(Optional.of(problemDetail));

        // when
        Optional<ProblemDetail> result = validator.validate(request);

        // then
        ProblemDetail resultProblemDetail = result.get();
        assertNotNull(resultProblemDetail);
        assertThat(resultProblemDetail.getTitle()).isEqualTo(title);
    }

    @Test
    public void shouldValidateRequestForValidSimpleMovingDayAverageData() {
        // given
        SimpleMovingDayAverageData data = null;
        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice("200.00")
                .data(data)
                .build();

        String title = "Simple Moving Day Average data must be present order to calculate the moving day average indicator";
        ProblemDetail problemDetail = ProblemDetailBuilder
                .problemDetailBuilder()
                .title(title)
                .build();

        when(dataValidationRule.validate(request)).thenReturn(Optional.of(problemDetail));

        // when
        Optional<ProblemDetail> result = validator.validate(request);

        // then
        ProblemDetail resultProblemDetail = result.get();
        assertNotNull(resultProblemDetail);
        assertThat(resultProblemDetail.getTitle()).isEqualTo(title);
    }

    @Test
    public void shouldValidateRequestAndReturnEmptyOptionalWhenRequestIsValid() {
        // given
        SmaData smaData = SmaData.builder()
                .date(LocalDate.now().toString())
                .simpleMovingDayAverage("100.00")
                .build();

        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice("200.00")
                .data(SimpleMovingDayAverageData.builder()
                        .technicalAnalysis(
                                TechnicalAnalysis.builder().simpleMovingDayAverages(List.of(smaData)).build())
                        .build())
                .build();

        // when
        Optional<ProblemDetail> result = validator.validate(request);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenSimpleMovingAverageDataDataIsMissingForCurrentDate() {
        // given
        SmaData smaData = SmaData.builder()
                .date(LocalDate.now().minusDays(1).toString())
                .simpleMovingDayAverage("100.00")
                .build();

        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice("200.00")
                .data(SimpleMovingDayAverageData.builder()
                        .technicalAnalysis(
                                TechnicalAnalysis.builder().simpleMovingDayAverages(List.of(smaData)).build())
                        .build())
                .build();

        String title = "Simple Moving Day Average data is missing for the current data";
        ProblemDetail problemDetail = ProblemDetailBuilder
                .problemDetailBuilder()
                .title(title)
                .build();

        when(currentDayValidationRule.validate(request)).thenReturn(Optional.of(problemDetail));


        // when
        Optional<ProblemDetail> result = validator.validate(request);

        // then
        ProblemDetail resultProblemDetail = result.get();
        assertNotNull(resultProblemDetail);
        assertThat(resultProblemDetail.getTitle()).isEqualTo(title);
    }

    private String getRandomValueOfEmptyOrNull() {
        List<String> tickerValues = new ArrayList();
        tickerValues.add("");
        tickerValues.add(null);
        return tickerValues.get(new Random().nextInt(tickerValues.size()));
    }
}
