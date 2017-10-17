package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import com.hubspot.dropwizard.guicier.objects.ExplicitDAO;
import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import io.dropwizard.testing.junit.ResourceTestRule;

/**
 * this test is created to address to Null Pointer Exceptions in JerseyTest.teardown() related to ServiceLocator
 * See: https://github.com/dropwizard/dropwizard/issues/828 and http://permalink.gmane.org/gmane.comp.java.dropwizard.devel/376
 */
public class InjectedResourcesTest {

    static {
        JerseyGuiceUtils.reset();
    }

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ExplicitResource(new ExplicitDAO()))
            .build();

    @Test
    public void shouldGetExplicitMessage() {
        // when
        String message = resources.client().target("/explicit/message").request().get(String.class);

        // then
        assertThat(message).isEqualTo("this DAO was bound explicitly");
    }
}
