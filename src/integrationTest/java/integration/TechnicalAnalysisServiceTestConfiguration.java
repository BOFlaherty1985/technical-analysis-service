package integration;

import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageCurrentDayValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.SimpleMovingDayAverageDataValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.StockPriceValidationRule;
import com.investment.technicalanalysisservice.sma.web.validation.rules.ValidationRule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.investment.technicalanalysisservice")
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class TechnicalAnalysisServiceTestConfiguration {

    @Bean
    public List<ValidationRule> validationRuleList() {
        List<ValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new StockPriceValidationRule());
        validationRules.add(new SimpleMovingDayAverageDataValidationRule());
        validationRules.add(new SimpleMovingDayAverageCurrentDayValidationRule());
        return validationRules;
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }


}

