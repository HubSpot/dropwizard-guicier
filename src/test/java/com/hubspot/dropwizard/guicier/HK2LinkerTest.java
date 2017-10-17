package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.ServletException;

import org.glassfish.hk2.api.ServiceLocator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import com.hubspot.dropwizard.guicier.objects.TestModule;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@Ignore
public class HK2LinkerTest {

    private static Injector injector;
    final ServiceLocator serviceLocator = injector.getInstance(ServiceLocator.class);

    @BeforeClass
    public static void setup() {

        final GuiceBundle bundle = GuiceBundle.defaultBuilder(Configuration.class).modules(new TestModule()).build();
        bundle.initialize(new Bootstrap<Configuration>(new Application<Configuration>() {
            @Override
            public void run(Configuration configuration, Environment environment) throws Exception {

            }
        }));
        injector = bundle.getInjector();

    }

    @AfterClass
    public static void tearDown() {
        JerseyGuiceUtils.reset();
    }

    @Test
    public void explicitGuiceBindingsAreBridgedToHk2() throws ServletException {
        // when
        ExplicitResource resource = serviceLocator.createAndInitialize(ExplicitResource.class);

        // then
        assertThat(resource).isNotNull();
        assertThat(resource.getDAO()).isNotNull();
    }
}
