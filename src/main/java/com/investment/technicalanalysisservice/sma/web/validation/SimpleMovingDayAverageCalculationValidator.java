package com.investment.technicalanalysisservice.sma.web.validation;

import com.investment.technicalanalysisservice.sma.web.validation.rules.ValidationRule;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import problemdetail.ProblemDetail;

import java.util.List;
import java.util.Optional;

@Component
public class SimpleMovingDayAverageCalculationValidator {

    private List<ValidationRule> validationRuleList;

    @Autowired
    public SimpleMovingDayAverageCalculationValidator(List<ValidationRule> validationRules) {
        this.validationRuleList = validationRules;
    }

    public Optional<ProblemDetail> validate(SimpleMovingDayAverageCalculatorRequest request) {
        for (ValidationRule rule : validationRuleList) {
            Optional<ProblemDetail> problemDetail = rule.validate(request);
            if (problemDetail.isPresent()) {
                return rule.validate(request);
            }
        }
        return Optional.empty();
    }
}
