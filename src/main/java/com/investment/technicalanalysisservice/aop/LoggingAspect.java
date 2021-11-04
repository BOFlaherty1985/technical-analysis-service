package com.investment.technicalanalysisservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before(value = "within(com.investment.technicalanalysisservice.sma.web.SimpleMovingDayAverageCalculationController)")
    public void beforeMethodLogging() {
        log.info("Before method...");
    }

    @After(value = "within(com.investment.technicalanalysisservice.sma.web.SimpleMovingDayAverageCalculationController)")
    public void afterMethodLogging() {
        log.info("After Method...");
    }

//    @Around(value = "within(com.investment.technicalanalysisservice.sma.web.SimpleMovingDayAverageCalculationController)")
//    public void logMethod() {
//        log.info("hit /test");
//    }
}
