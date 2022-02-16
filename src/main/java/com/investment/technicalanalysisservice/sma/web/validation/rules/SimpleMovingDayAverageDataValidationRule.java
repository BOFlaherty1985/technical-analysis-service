package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;

import java.util.Optional;

public class SimpleMovingDayAverageDataValidationRule implements ValidationRule {

    @Override
    public Optional<ProblemDetail> validate(SimpleMovingDayAverageCalculatorRequest request) {
        if (request.isSimpleMovingDayAverageDataMissing() || request.isTechnicalAnalysisDataMissing()) {
            return Optional.of(ProblemDetailBuilder.problemDetailBuilder()
                    // TODO - complete ProblemDetail object
                    .title("Simple Moving Day Average data must be present order to calculate the moving day average indicator")
                    .build());
        }
        return Optional.empty();
    }
}
