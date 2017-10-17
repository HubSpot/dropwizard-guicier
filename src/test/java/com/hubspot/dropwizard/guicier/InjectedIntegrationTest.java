package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import javax.ws.rs.client.Client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.io.Resources;
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

        // when
        final String message = client.target(
                String.format("http://localhost:%d//explicit/message", RULE.getLocalPort()))
                .request()
                .get(String.class);

        // then
        assertThat(message).isEqualTo("this DAO was bound explicitly");
    }
}
