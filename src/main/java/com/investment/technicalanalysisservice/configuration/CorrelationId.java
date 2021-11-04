package com.investment.technicalanalysisservice.configuration;

import java.util.UUID;

public class CorrelationId {

    public static final ThreadLocal<UUID> CORRELATION_ID = new ThreadLocal<>();

    public UUID getCorrelationId() {
        return CORRELATION_ID.get();
    }

    public void setCorrelationId(UUID correlationId) {
        CORRELATION_ID.set(correlationId);
    }
}
