package com.investment.technicalanalysisservice.sma.service;

import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.technicalanalysisservice.sma.web.validation.SimpleMovingDayAverageCalculationValidator;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import problemdetail.ProblemDetail;
import server.technicalanalysis.Indicator;
import server.technicalanalysis.TechnicalAnalysisServerResponse;
import server.technicalanalysis.TechnicalAnalysisServerResponseBuilder;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@Slf4j
public class SimpleMovingDayAverageCalculationService {

    private Clock clock;
    private SimpleMovingDayAverageCalculationValidator validator;

    public SimpleMovingDayAverageCalculationService(Clock clock, SimpleMovingDayAverageCalculationValidator validator) {
        this.clock = clock;
        this.validator = validator;
    }

    public TechnicalAnalysisServerResponse calculate(SimpleMovingDayAverageData data, String stockPrice) {

        Optional<ProblemDetail> validationResult = validator.validate(SimpleMovingDayAverageCalculatorRequest.builder()
                .data(data)
                .stockPrice(stockPrice)
                .build());

        if (validationResult.isPresent()) {
            return TechnicalAnalysisServerResponseBuilder.builder()
                    .problemDetail(Optional.of(validationResult.get()))
                    .build();
        }

        Double price = Double.valueOf(stockPrice);
        Double simpleMovingDayAverage = extractSimpleMovingDayAverage(data).orElse(Double.NaN);

        return TechnicalAnalysisServerResponseBuilder.builder()
                .indicator(Optional.of(determineSmaIndicator.apply(price, simpleMovingDayAverage)))
                .problemDetail(Optional.empty())
                .build();
    }

    private Optional<Double> extractSimpleMovingDayAverage(SimpleMovingDayAverageData data) {
        Optional<Double> simpleMovingDayAverages = data.getTechnicalAnalysis().getSimpleMovingDayAverages().stream()
                .filter(smaData -> smaData.getDate().equals(calculateSimpleMovingDayAverageDateFor.apply(LocalDate.now(clock))))
                .findFirst()
                .map(smaData -> Double.valueOf(smaData.getSimpleMovingDayAverage()));
        return simpleMovingDayAverages;
    }

    private BiFunction<Double, Double, Indicator> determineSmaIndicator =
            (aDouble, aDouble2) -> (aDouble >= aDouble2) ? Indicator.BULLISH : Indicator.BEARISH;

    private Function<LocalDate, String> calculateSimpleMovingDayAverageDateFor = (currentDate) -> currentDate.getDayOfWeek() == DayOfWeek.MONDAY
            ? currentDate.minusDays(3).toString()
            : currentDate.minusDays(1).toString();
}

