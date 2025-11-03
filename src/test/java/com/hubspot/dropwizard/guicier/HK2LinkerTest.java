package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import com.hubspot.dropwizard.guicier.objects.HK2ContextBindings;
import com.hubspot.dropwizard.guicier.objects.TestApplication;
import io.dropwizard.core.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import javax.servlet.ServletException;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class HK2LinkerTest {

  private static final DropwizardAppExtension<Configuration> EXT =
    new DropwizardAppExtension<>(
      TestApplication.class,
      ResourceHelpers.resourceFilePath("test-config.yml")
    );

  private Injector injector;
  private InjectionManager injectionManager;

  @BeforeEach
  public void setup() {
    TestApplication testApplication = EXT.getApplication();
    GuiceBundle<Configuration> guiceBundle = testApplication.getGuiceBundle();

    injector = guiceBundle.getInjector();
    injectionManager = injector.getInstance(InjectionManager.class);
  }

  @Test
  public void explicitGuiceBindingsAreBridgedToHk2() throws ServletException {
    ExplicitResource resource = injectionManager.createAndInitialize(
      ExplicitResource.class
    );

    assertThat(resource).isNotNull();
    assertThat(resource.getDAO()).isNotNull();
  }

  @Test
  public void contextBindingsAreBridgedToGuice() {
    for (Class<?> clazz : HK2ContextBindings.SET) {
      Binding<?> binding = injector.getExistingBinding(Key.get(clazz));
      assertThat(binding).as("%s has a Guice binding", clazz.getName()).isNotNull();
    }
  }
}
