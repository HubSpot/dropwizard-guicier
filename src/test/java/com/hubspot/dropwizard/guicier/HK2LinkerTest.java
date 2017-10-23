package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
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

    @Test
    public void contextBindingsAreBridgedToGuice() {
        // This list comes from https://stackoverflow.com/a/35868654
        List<Class<?>> classes = Arrays.asList(
            Application.class,
            javax.ws.rs.core.Configuration.class,
            ContainerRequestContext.class,
            HttpHeaders.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            Providers.class,
            Request.class,
            ResourceContext.class,
            SecurityContext.class,
            ServletConfig.class,
            ServletContext.class,
            UriInfo.class,
            // Jersey-specific
            ExtendedUriInfo.class);
        for (Class<?> clazz : classes) {
            Binding binding = injector.getExistingBinding(Key.get(clazz));
            assertThat(binding)
                .as("%s has a guice binding", clazz.getName())
                .isNotNull();
        }
    }
}
