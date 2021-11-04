package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;

import java.util.Optional;

import static com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest.stockPriceNullOrEmpty;

public class StockPriceValidationRule implements ValidationRule {

    @Override
    public Optional<ProblemDetail> validate(SimpleMovingDayAverageCalculatorRequest request) {
        if (stockPriceNullOrEmpty.test(request.getStockPrice())) {
            return Optional.of(ProblemDetailBuilder.problemDetailBuilder()
                    // TODO - complete ProblemDetail object
                    .title("A stock price must be supplied in order to calculate the moving day average indicator")
                    .build());
        }
        return Optional.empty();
    }
}
