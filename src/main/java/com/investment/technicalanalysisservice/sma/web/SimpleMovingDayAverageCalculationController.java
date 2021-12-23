package com.investment.technicalanalysisservice.sma.web;

import com.investment.alphavantageapi.api.company.CompanyOverviewApi;
import com.investment.alphavantageapi.api.sma.SimpleMovingDayAverageApi;
import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.technicalanalysisservice.configuration.TestController;
import com.investment.technicalanalysisservice.sma.service.SimpleMovingDayAverageCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import problemdetail.ProblemDetail;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.util.Optional;
import java.util.UUID;

import static com.investment.technicalanalysisservice.configuration.CorrelationId.CORRELATION_ID;
import static problemdetail.ProblemDetailBuilder.problemDetailBuilder;

@RestController
@Slf4j
public class SimpleMovingDayAverageCalculationController extends TestController {

    private SimpleMovingDayAverageApi alphaVantageSimpleMovingDayAverageApi;
    private SimpleMovingDayAverageCalculationService smaCalculationService;

    @Autowired
    public SimpleMovingDayAverageCalculationController(SimpleMovingDayAverageApi alphaVantageSimpleMovingDayAverageApi,
                                                       SimpleMovingDayAverageCalculationService smaCalculationService) {
        this.alphaVantageSimpleMovingDayAverageApi = alphaVantageSimpleMovingDayAverageApi;
        this.smaCalculationService = smaCalculationService;
    }

    // TODO pass through length of moving day average, i.e. 50, 100, 200
    @GetMapping(value = "/simpleMovingDayAnalysis")
    public ResponseEntity<TechnicalAnalysisServerResponse> processSimpleMovingDayAverageAnalysis(@RequestParam String ticker,
                                                                                                 @RequestParam String stockPrice) {
        SimpleMovingDayAverageData data = alphaVantageSimpleMovingDayAverageApi.getSimpleMovingDayAverageFor(ticker);
        TechnicalAnalysisServerResponse response = smaCalculationService.calculate(data, stockPrice);

        // add test
        if (response.getProblemDetail().isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok().contentType(MediaType.valueOf("application/problem+json")).body(response);
        }
    }

    @GetMapping(value = "/test", produces = "application/test123+json")
    public ResponseEntity<TechnicalAnalysisServerResponse> getTest() {
        TechnicalAnalysisServerResponse technicalAnalysisResponse = new TechnicalAnalysisServerResponse();

        UUID correlation_id = CORRELATION_ID.get();
        log.info("correlation_id: " + correlation_id);

        Optional<ProblemDetail> problemDetail = Optional.of(problemDetailBuilder()
                .type("about:blank")
                .title("Test Error")
                .detail("A simple test error to see if this works")
                .status("500")
                .instance("/test/endpoint")
                .build());
        technicalAnalysisResponse.setProblemDetail(problemDetail);

        return ResponseEntity.ok(technicalAnalysisResponse);
        //throw new IllegalArgumentException("test");
    }
}
