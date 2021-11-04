package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import problemdetail.ProblemDetail;

import java.util.Optional;

public interface ValidationRule {

    Optional<ProblemDetail> validate(SimpleMovingDayAverageCalculatorRequest request);

}
