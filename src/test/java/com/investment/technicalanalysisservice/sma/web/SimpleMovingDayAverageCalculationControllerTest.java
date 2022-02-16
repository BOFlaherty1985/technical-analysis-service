package com.investment.technicalanalysisservice.sma.web;

import com.investment.alphavantageapi.api.sma.SimpleMovingDayAverageApi;
import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.technicalanalysisservice.sma.service.SimpleMovingDayAverageCalculationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import server.technicalanalysis.Indicator;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMovingDayAverageCalculationControllerTest {

    private static final String STOCK_TICKER = "IBM";

    @InjectMocks
    private SimpleMovingDayAverageCalculationController controller;

    @Mock
    private SimpleMovingDayAverageApi alphaVantageApi;

    @Mock
    private SimpleMovingDayAverageCalculationService simpleMovingDayAverageCalculationService;

    /*
        Controller - receive request from BFF service
        /technical-analysis-service/{ticker}/simpleMovingDayAverage/{value}

        Controller calls Service component, RestTemplate call is made to AlphaVantage Service
        /alphavantage/{ticker}/simpleMovingDayAverage/{value}

        Controller calls SimpleMovingDayAverageCalculationService
            - Service retrieves current price of {ticker} stock
            - Service performs calculation to determine whether the {ticker} price is above/below the simple moving day (sma) average.
                - Above = BULLISH sign
                - Below = BEARISH sign

     */
    @Test
    public void shouldReceiveTickerPathVariableAndCallAlphaVantageService() {
        String ticker = STOCK_TICKER;
        String stockPrice = "200.00";

        TechnicalAnalysisServerResponse response = new TechnicalAnalysisServerResponse();
        response.setIndicator(Optional.of(Indicator.BULLISH));
        response.setProblemDetail(Optional.empty());
        given(simpleMovingDayAverageCalculationService.calculate(any(), any())).willReturn(response);

        ResponseEntity<TechnicalAnalysisServerResponse> result = controller.processSimpleMovingDayAverageAnalysis(ticker, stockPrice);
        assertNotNull(result);
        verify(alphaVantageApi).getSimpleMovingDayAverageFor(ticker);
    }

    @Test
    public void shouldCallSimpleMovingDayAverageCalculationService() {
        String ticker = STOCK_TICKER;
        String stockPrice = "200.00";
        SimpleMovingDayAverageData data = SimpleMovingDayAverageData.builder().build();

        TechnicalAnalysisServerResponse response = new TechnicalAnalysisServerResponse();
        response.setIndicator(Optional.of(Indicator.BULLISH));
        response.setProblemDetail(Optional.empty());
        given(simpleMovingDayAverageCalculationService.calculate(any(), any())).willReturn(response);
        when(alphaVantageApi.getSimpleMovingDayAverageFor(ticker)).thenReturn(data);

        controller.processSimpleMovingDayAverageAnalysis(ticker, stockPrice);
        verify(simpleMovingDayAverageCalculationService).calculate(data, stockPrice);
    }

    // TODO - add test for "application/problem+json response

}
