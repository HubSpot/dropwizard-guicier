package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import com.hubspot.dropwizard.guicier.objects.ExplicitDAO;
import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * this test is created to address to Null Pointer Exceptions in JerseyTest.teardown() related to ServiceLocator
 * See: https://github.com/dropwizard/dropwizard/issues/828 and http://permalink.gmane.org/gmane.comp.java.dropwizard.devel/376
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class InjectedResourcesTest {

  private static final ResourceExtension EXT = ResourceExtension
    .builder()
    .addResource(new ExplicitResource(new ExplicitDAO()))
    .build();

  @Test
  public void shouldGetExplicitMessage() {
    // when
    String message = EXT.client().target("/explicit/message").request().get(String.class);

    // then
    assertThat(message).isEqualTo("this DAO was bound explicitly");
  }
}
