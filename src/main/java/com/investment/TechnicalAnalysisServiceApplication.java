package com.investment;

import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageCurrentDayValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageDataValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.StockPriceValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.ValidationRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@ServletComponentScan
public class TechnicalAnalysisServiceApplication {

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	public List<ValidationRule> validationRuleList() {
		List<ValidationRule> validationRules = new ArrayList<>();
		validationRules.add(new StockPriceValidationRule());
		validationRules.add(new SimpleMovingDayAverageDataValidationRule());
		validationRules.add(new SimpleMovingDayAverageCurrentDayValidationRule());
		return validationRules;
	}

	public static void main(String[] args) {
		SpringApplication.run(TechnicalAnalysisServiceApplication.class, args);
	}

}
