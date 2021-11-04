package com.investment.technicalanalysisservice.configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class TestController {

    public UUID test(HttpServletRequest request) {
        return UUID.fromString(request.getHeader("correlation-id"));
    }

}
