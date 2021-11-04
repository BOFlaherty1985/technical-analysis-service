package integration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
@ComponentScan(basePackages = "com.investment.technicalanalysisservice")
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class TechnicalAnalysisServiceTestConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }


}

