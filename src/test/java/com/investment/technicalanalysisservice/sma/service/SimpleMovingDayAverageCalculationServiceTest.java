package com.investment.technicalanalysisservice.sma.service;

import com.investment.alphavantageapi.model.sma.MetaData;
import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.alphavantageapi.model.sma.SmaData;
import com.investment.alphavantageapi.model.sma.TechnicalAnalysis;
import com.investment.technicalanalysisservice.sma.web.validation.SimpleMovingDayAverageCalculationValidator;
import com.investment.technicalanalysisservice.sma.web.validator.SimpleMovingDayAverageCalculatorRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;
import server.technicalanalysis.Indicator;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMovingDayAverageCalculationServiceTest {

    @InjectMocks
    private SimpleMovingDayAverageCalculationService calculationService;

    @Mock
    private SimpleMovingDayAverageCalculationValidator validator;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @Test
    public void shouldValidateRequestObject() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.JUNE, 2); // Wednesday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        SmaData smaData = SmaData.builder()
                .date(currentDate.minusDays(1).toString())
                .simpleMovingDayAverage("190.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        // when
        calculationService.calculate(data, "200.00");

        // verify
        verify(validator).validate(any());
    }


    @Test
    public void shouldFailValidationAndReturnResponseWithProblemDetail() {
        // given
        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest
                .builder()
                .stockPrice(null)
                .data(null)
                .build();

        Optional<ProblemDetail> problemDetail = Optional.of(ProblemDetailBuilder.problemDetailBuilder().build());
        when(validator.validate(request)).thenReturn(problemDetail);

        // when
        TechnicalAnalysisServerResponse result = calculationService.calculate(request.getData(), request.getStockPrice());

        // then
        assertNotNull(result.getProblemDetail());
        assertEquals(problemDetail, result.getProblemDetail());
    }

    @Test
    public void shouldReturnBullishSignWhenStockPriceIsAboveSimpleMovingDayAverage() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.JUNE, 2); // Wednesday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        SmaData smaData = SmaData.builder()
                .date(currentDate.minusDays(1).toString())
                .simpleMovingDayAverage("190.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        String stockPrice = "200.00";
        SimpleMovingDayAverageCalculatorRequest request = SimpleMovingDayAverageCalculatorRequest
                .builder()
                .stockPrice(stockPrice)
                .data(data)
                .build();

        when(validator.validate(request)).thenReturn(Optional.empty());

        // when
        TechnicalAnalysisServerResponse result = calculationService.calculate(data, stockPrice);

        // then
        assertNotNull(result);
        assertEquals(result.getIndicator(), Optional.of(Indicator.BULLISH));
    }

    @Test
    public void shouldReturnBullishSignWhenStockPriceIsAboveSimpleMovingDayAverageAndDateRequestedIsAMonday() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.MAY, 31); // Monday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        SmaData smaData = SmaData.builder()
                .date(currentDate.minusDays(3).toString()) // previous Friday
                .simpleMovingDayAverage("190.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        // when
        TechnicalAnalysisServerResponse result = calculationService.calculate(data, "200.00");

        // then
        assertNotNull(result);
        assertEquals(result.getIndicator(), Optional.of(Indicator.BULLISH));
    }

    @Test
    public void shouldReturnBullishSignWhenStockPriceIsEqualToSimpleMovingDayAverage() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.JANUARY, 02); // Monday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        SmaData smaData = SmaData.builder()
                .date("2021-01-01")
                .simpleMovingDayAverage("200.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        // when
        TechnicalAnalysisServerResponse result = calculationService.calculate(data, "200.00");

        // then
        assertNotNull(result);
        assertEquals(result.getIndicator(), Optional.of(Indicator.BULLISH));
    }

    @Test
    public void shouldReturnBearishSignWhenStockPriceIsEqualToSimpleMovingDayAverage() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.JANUARY, 02); // Monday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        SmaData smaData = SmaData.builder()
                .date("2021-01-01")
                .simpleMovingDayAverage("210.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        // when
        TechnicalAnalysisServerResponse result = calculationService.calculate(data, "200.00");

        // then
        assertNotNull(result);
        assertEquals(result.getIndicator(), Optional.of(Indicator.BEARISH));
    }

    @Test
    public void shouldReturnProblemDetailWhenNoSimpleMovingDataIsAvailableForCurrentDate() {
        // given
        LocalDate currentDate = LocalDate.of(2021, Month.JANUARY, 02); // Monday
        fixedClock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        SmaData smaData = SmaData.builder()
                .date(currentDate.minusDays(1).toString())
                .simpleMovingDayAverage("100.00")
                .build();

        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData))
                        .build())
                .build();

        String stockPrice = "200.00";
        SimpleMovingDayAverageCalculatorRequest request =
                SimpleMovingDayAverageCalculatorRequest.builder()
                .data(data)
                .stockPrice(stockPrice)
                .build();

        when(validator.validate(request)).thenReturn(Optional.of(ProblemDetailBuilder.problemDetailBuilder().build()));

        // when
        TechnicalAnalysisServerResponse response = calculationService.calculate(data, stockPrice);

        // then
        assertNotNull(response.getProblemDetail());
    }

}
