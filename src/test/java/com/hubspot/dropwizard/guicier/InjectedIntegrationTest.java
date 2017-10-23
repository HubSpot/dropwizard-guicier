package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import javax.ws.rs.client.Client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.io.Resources;
import com.hubspot.dropwizard.guicier.objects.HK2ContextBindings;
import com.hubspot.dropwizard.guicier.objects.TestApplication;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class InjectedIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE =
            new DropwizardAppRule<>(TestApplication.class, resourceFilePath("test-config.yml"));

    protected static Client client;

    @BeforeClass
    public static void setUp() {
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
    }

    @AfterClass
    public static void tearDown() {
        JerseyGuiceUtils.reset();
    }

    public static String resourceFilePath(String resourceClassPathLocation) {
        try {
            return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldGetExplicitMessage() {
        String message = client.target(getUri("/explicit/message")).request().get(String.class);
        assertThat(message).isEqualTo("this DAO was bound explicitly");
    }

    @Test
    public void hk2ContextBindingsAreResolvableInGuice() {
        for (Class<?> clazz : HK2ContextBindings.SET) {
            boolean resolvable = client.target(getUri("/jersey-context/is-resolvable-by-guice"))
                .queryParam("className", clazz.getName())
                .request()
                .get(Boolean.class);
            assertThat(resolvable)
                .as("%s is resolvable by Guice", clazz.getName())
                .isTrue();
        }
    }

    private static String getUri(String path) {
        String domain = "http://localhost:" + RULE.getLocalPort();
        return domain + (path.startsWith("/") ? "" : "/") + path;
    }
}
