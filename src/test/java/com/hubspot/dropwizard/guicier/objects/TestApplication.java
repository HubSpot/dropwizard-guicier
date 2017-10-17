package com.hubspot.dropwizard.guicier.objects;

import com.hubspot.dropwizard.guicier.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<Configuration> {

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        final GuiceBundle<Configuration> jersey2GuiceBundle = GuiceBundle.defaultBuilder(Configuration.class)
            .modules(new TestModule())
            .build();
        bootstrap.addBundle(jersey2GuiceBundle);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {

    }
}
