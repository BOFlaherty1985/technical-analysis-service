package com.investment.technicalanalysisservice.configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/test")
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "correlation-id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String correlation_id = httpServletRequest.getHeader(CORRELATION_ID_HEADER);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (correlation_id == null) {
            correlation_id = UUID.randomUUID().toString();
        }

        CorrelationId.CORRELATION_ID.set(UUID.fromString(correlation_id));

        httpServletResponse.addHeader(CORRELATION_ID_HEADER, correlation_id);
        chain.doFilter(request, response);
    }
}
