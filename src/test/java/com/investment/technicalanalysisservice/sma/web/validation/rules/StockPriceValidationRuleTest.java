package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import org.junit.Test;
import problemdetail.ProblemDetail;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StockPriceValidationRuleTest {

    private StockPriceValidationRule validationRule = new StockPriceValidationRule();

    @Test
    public void shouldFailValidationWhenStockPriceIsNull() {
        // given
        SimpleMovingDayAverageCalculatorRequest request
                = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice(null)
                .build();

        // when
        Optional<ProblemDetail> result = validationRule.validate(request);

        // then
        ProblemDetail problemDetail = result.get();
        assertNotNull(problemDetail);
        assertEquals("A stock price must be supplied in order to calculate the moving day average indicator", problemDetail.getTitle());
    }

    @Test
    public void shouldFailValidationWhenStockPriceIsEmpty() {
        // given
        SimpleMovingDayAverageCalculatorRequest request
                = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice("")
                .build();

        // when
        Optional<ProblemDetail> result = validationRule.validate(request);

        // then
        ProblemDetail problemDetail = result.get();
        assertNotNull(problemDetail);
        assertEquals("A stock price must be supplied in order to calculate the moving day average indicator", problemDetail.getTitle());
    }

    @Test
    public void shouldPassValidationWhenStockPriceIsValid() {
        // given
        SimpleMovingDayAverageCalculatorRequest request
                = SimpleMovingDayAverageCalculatorRequest.builder()
                .stockPrice("200.00")
                .build();

        // when
        Optional<ProblemDetail> result = validationRule.validate(request);

        // then
        assertEquals(Optional.empty(), result);
    }
}
