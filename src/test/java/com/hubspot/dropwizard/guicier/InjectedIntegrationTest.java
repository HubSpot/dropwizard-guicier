package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import com.hubspot.dropwizard.guicier.objects.ComponentInvocationCounter;
import com.hubspot.dropwizard.guicier.objects.ContextInjectedFilter;
import com.hubspot.dropwizard.guicier.objects.HK2ContextBindings;
import com.hubspot.dropwizard.guicier.objects.TestApplication;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class InjectedIntegrationTest {

  private static final DropwizardAppExtension<Configuration> EXT =
    new DropwizardAppExtension<>(
      TestApplication.class,
      ResourceHelpers.resourceFilePath("test-config.yml")
    );

  protected static Client client;

  @BeforeAll
  public static void setUp() {
    client = EXT.client();
  }

  @Test
  public void shouldGetExplicitMessage() {
    String message = client
      .target(getUri("/explicit/message"))
      .request()
      .get(String.class);
    assertThat(message).isEqualTo("this DAO was bound explicitly");
  }

  @Test
  public void emptyCtorGoogleInject() {
    assertThat(
      client.target(getUri("/empty-ctor-google-inject")).request().get(String.class)
    )
      .isEqualTo("world");
  }

  @Test
  public void testContextInjectedFilter() {
    Response response = client.target(getUri("/explicit/message")).request().get();
    assertThat(response.getStatusInfo().getFamily()).isEqualTo(Family.SUCCESSFUL);
    assertThat(response.getHeaderString(ContextInjectedFilter.RESPONSE_HEADER))
      .isEqualTo("true");

    TestApplication app = EXT.getApplication();
    ComponentInvocationCounter counter = app
      .getGuiceBundle()
      .getInjector()
      .getInstance(ComponentInvocationCounter.class);
    assertThat(counter.count("ContextInjectedFilter")).isGreaterThanOrEqualTo(1);
  }

  @Test
  public void hk2ContextBindingsAreResolvableInGuice() {
    Assertions.assertAll(
      HK2ContextBindings.SET
        .stream()
        .map(clazz ->
          () -> {
            boolean resolvable = client
              .target(getUri("/jersey-context/is-resolvable-by-guice"))
              .queryParam("className", clazz.getName())
              .request()
              .get(Boolean.class);
            assertThat(resolvable)
              .as("%s is resolvable by Guice", clazz.getName())
              .isTrue();
          }
        )
    );
  }

  private static String getUri(String path) {
    String domain = "http://localhost:" + EXT.getLocalPort();
    return domain + (path.startsWith("/") ? "" : "/") + path;
  }
}
