package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;

import org.glassfish.hk2.api.ServiceLocator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import com.hubspot.dropwizard.guicier.objects.TestModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import io.dropwizard.Configuration;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HK2LinkerTest {

    private Injector injector;
    private ServiceLocator serviceLocator;

    @Before
    public void setup() throws Exception {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        Environment environment = new Environment("test env", objectMapper, null, new MetricRegistry(), null);
        GuiceBundle guiceBundle = GuiceBundle.defaultBuilder(Configuration.class)
            .modules(new TestModule())
            .build();
        Bootstrap bootstrap = mock(Bootstrap.class);
        when(bootstrap.getObjectMapper()).thenReturn(objectMapper);
        guiceBundle.initialize(bootstrap);
        guiceBundle.run(new Configuration(), environment);

        injector = guiceBundle.getInjector();
        serviceLocator = injector.getInstance(ServiceLocator.class);
    }

    @AfterClass
    public static void tearDown() {
        JerseyGuiceUtils.reset();
    }

    @Test
    public void explicitGuiceBindingsAreBridgedToHk2() throws ServletException {
        ExplicitResource resource = serviceLocator.createAndInitialize(ExplicitResource.class);

        assertThat(resource).isNotNull();
        assertThat(resource.getDAO()).isNotNull();
    }
}
