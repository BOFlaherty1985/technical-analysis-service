package integration;

import com.investment.alphavantageapi.api.sma.SimpleMovingDayAverageApi;
import com.investment.alphavantageapi.model.sma.MetaData;
import com.investment.alphavantageapi.model.sma.SimpleMovingDayAverageData;
import com.investment.alphavantageapi.model.sma.SmaData;
import com.investment.alphavantageapi.model.sma.TechnicalAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.time.LocalDate.now;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = TechnicalAnalysisServiceTestConfiguration.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TechnicalAnalysisServiceIntegrationTest {

    private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    @Autowired(required = true)
    private MockMvc mockMvc;

    @MockBean
    private SimpleMovingDayAverageApi alphaVantageSimpleMovingDayAverageApi;

    @Test
    public void shouldReturnSimpleMovingDayAverageResultWithErrorMessageWhenNoStockPriceIsProvided() throws Exception {
        String ticker = "IBM";

        when(alphaVantageSimpleMovingDayAverageApi.getSimpleMovingDayAverageFor(ticker)).thenReturn(SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().symbol(ticker).build()).build());

        mockMvc.perform(get(String.format("/simpleMovingDayAnalysis?ticker=%s&stockPrice=%s", ticker, "")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(content().string(containsString("A stock price must be supplied in order to calculate the moving day average indicator")));
    }

    @Test
    public void shouldReturnSimpleMovingDayAverageResultWithErrorWhenNoAlphaVantageDataPresent() throws Exception {
        String ticker = "IBM";

        when(alphaVantageSimpleMovingDayAverageApi.getSimpleMovingDayAverageFor(ticker)).thenReturn(SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().symbol(ticker).build())
                .technicalAnalysis(TechnicalAnalysis.builder().build()).build());

        mockMvc.perform(get("/simpleMovingDayAnalysis?ticker=" + ticker + "&stockPrice=200.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().string(containsString("Simple Moving Day Average data must be present order to calculate the moving day average indicator")));
    }

    @Test
    public void shouldReturnSimpleMovingDayAverageResultWithBullishIndicator() throws Exception {
        String ticker = "IBM";

        SmaData smaData = SmaData.builder()
                .date(now().minusDays(1).toString())
                .simpleMovingDayAverage("190.00")
                .build();

        when(alphaVantageSimpleMovingDayAverageApi.getSimpleMovingDayAverageFor(ticker)).thenReturn(SimpleMovingDayAverageData.builder()
                .metaData(MetaData.builder().symbol(ticker).build())
                .technicalAnalysis(TechnicalAnalysis.builder()
                        .simpleMovingDayAverages(List.of(smaData)).build())
                .build());

        mockMvc.perform(get("/simpleMovingDayAnalysis?ticker=" + ticker + "&stockPrice=200.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("BULLISH")));
    }

}
