package com.investment.technicalanalysisservice.sma.web.validation.rules;

import com.investment.technicalanalysisservice.api.model.sma.SmaData;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class SimpleMovingDayAverageCurrentDayValidationRule implements ValidationRule {

    @Override
    public Optional<ProblemDetail> validate(SimpleMovingDayAverageCalculatorRequest request) {
        Optional<SmaData> currentDateSma = request.getData()
                .getTechnicalAnalysis()
                .getSimpleMovingDayAverages()
                .stream().filter(smaData -> isSimpleMovingDayDataPresentForToday.test(smaData.getDate()))
                .findFirst();

        if (!currentDateSma.isPresent()) {
            return Optional.of(ProblemDetailBuilder.problemDetailBuilder()
                    .title("Simple Moving Day Average data is missing for the current data")
                    .build());
        }

        return Optional.empty();
    }

    Predicate<String> isSimpleMovingDayDataPresentForToday = date -> date.equals(LocalDate.now().toString());

}
