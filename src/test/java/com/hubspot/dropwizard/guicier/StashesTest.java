package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import com.hubspot.dropwizard.guicier.objects.StashesTestModule;
import com.hubspot.dropwizard.guicier.objects.TestApplication;
import io.dropwizard.core.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StashesTest {

  private static final DropwizardAppExtension<Configuration> EXT =
    new DropwizardAppExtension<>(
      TestApplication.class,
      ResourceHelpers.resourceFilePath("test-config.yml")
    );

  private Client client;

  @BeforeEach
  public void setup() {
    this.client = EXT.client();
  }

  private int doGet(String path) {
    return client
      .target("http://localhost:" + EXT.getLocalPort() + "/stashes/" + path)
      .request()
      .get(int.class);
  }

  @Test
  public void itConstructorInjectsAStashedInt() {
    assertThat(doGet("itConstructorInjectsAStashedInt"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itConstructorInjectsAStashedIntGuiceProvider() {
    assertThat(doGet("itConstructorInjectsAStashedIntGuiceProvider"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itConstructorInjectsAStashedIntJavaxProvider() {
    assertThat(doGet("itConstructorInjectsAStashedIntJavaxProvider"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itFieldInjectsAStashedInt() {
    assertThat(doGet("itFieldInjectsAStashedInt"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itFieldInjectsAStashedIntGuiceProvider() {
    assertThat(doGet("itFieldInjectsAStashedIntGuiceProvider"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itFieldInjectsAStashedIntJavaxProvider() {
    assertThat(doGet("itFieldInjectsAStashedIntJavaxProvider"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }

  @Test
  public void itMethodInjectsAStashedInt() {
    assertThat(doGet("itMethodInjectsAStashedInt"))
      .isEqualTo(StashesTestModule.STASHED_INT_VALUE);
  }
}
