package com.hubspot.dropwizard.guicier.aop;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.core.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AopTest {

  private static final DropwizardAppExtension<Configuration> EXT =
    new DropwizardAppExtension<>(
      AopTestApplication.class,
      ResourceHelpers.resourceFilePath("test-config.yml")
    );

  protected static Client client;
  protected static AopTestApplication app;

  @BeforeAll
  public static void setUp() {
    client = EXT.client();
  }

  @Test
  public void itInterceptsMethod() {
    String response = client
      .target("http://localhost:" + EXT.getLocalPort() + MyResource.PATH)
      .request()
      .get(String.class);
    assertThat(response).isEqualTo(MyResource.RESPONSE);

    AopTestApplication app = EXT.getApplication();
    assertThat(app.getInterceptor().counter.get()).isEqualTo(1);
  }
}
