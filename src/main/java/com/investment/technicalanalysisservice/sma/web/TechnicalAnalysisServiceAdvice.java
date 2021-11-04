package com.investment.technicalanalysisservice.sma.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import problemdetail.ProblemDetail;
import problemdetail.ProblemDetailBuilder;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.util.Optional;

@ControllerAdvice
public class TechnicalAnalysisServiceAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<TechnicalAnalysisServerResponse> advice() {

        ProblemDetail problemDetail = ProblemDetailBuilder
                .problemDetailBuilder()
                .type("type")
                .title("title")
                .status("500")
                .detail("detail")
                .instance("/test/test")
                .build();

        TechnicalAnalysisServerResponse response = new TechnicalAnalysisServerResponse();
        response.setProblemDetail(Optional.of(problemDetail));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/problem+json")).body(response);

    }
}