package integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = TechnicalAnalysisServiceTestConfiguration.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class SimpleMovingDayAverageCalculationServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnProblemJsonContentTypeWhenExceptionThrown() throws Exception {

        // when
        mockMvc.perform(get("/test"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType("application/problem+json"));

    }
}
