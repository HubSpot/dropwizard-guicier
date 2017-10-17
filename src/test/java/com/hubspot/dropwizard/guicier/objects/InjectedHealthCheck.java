package com.hubspot.dropwizard.guicier.objects;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

public class InjectedHealthCheck extends HealthCheck {

    @Inject
    InjectedHealthCheck() {}

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
