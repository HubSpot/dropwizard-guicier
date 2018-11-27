package com.hubspot.dropwizard.guicier.objects;

import com.codahale.metrics.health.HealthCheck;

public class ProvidedHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
