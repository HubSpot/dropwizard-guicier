package com.hubspot.dropwizard.guicier;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Injector;
import com.hubspot.dropwizard.guicier.objects.ExplicitResource;
import com.hubspot.dropwizard.guicier.objects.InjectedHealthCheck;
import com.hubspot.dropwizard.guicier.objects.InjectedManaged;
import com.hubspot.dropwizard.guicier.objects.InjectedProvider;
import com.hubspot.dropwizard.guicier.objects.InjectedServerLifecycleListener;
import com.hubspot.dropwizard.guicier.objects.InjectedTask;
import com.hubspot.dropwizard.guicier.objects.InstanceManaged;
import com.hubspot.dropwizard.guicier.objects.InstanceManaged;
import com.hubspot.dropwizard.guicier.objects.ProvidedHealthCheck;
import com.hubspot.dropwizard.guicier.objects.ProvidedHealthCheck;
import com.hubspot.dropwizard.guicier.objects.ProvidedManaged;
import com.hubspot.dropwizard.guicier.objects.ProvidedManaged;
import com.hubspot.dropwizard.guicier.objects.ProvidedProvider;
import com.hubspot.dropwizard.guicier.objects.ProvidedProvider;
import com.hubspot.dropwizard.guicier.objects.ProvidedServerLifecycleListener;
import com.hubspot.dropwizard.guicier.objects.ProvidedServerLifecycleListener;
import com.hubspot.dropwizard.guicier.objects.ProvidedTask;
import com.hubspot.dropwizard.guicier.objects.ProvidedTask;
import com.hubspot.dropwizard.guicier.objects.ProviderManaged;
import com.hubspot.dropwizard.guicier.objects.ProviderManaged;
import com.hubspot.dropwizard.guicier.objects.TestApplication;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.util.Set;
import java.util.Set;
import java.util.function.Function;
import javax.servlet.ServletException;
import javax.servlet.ServletException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@ExtendWith(DropwizardExtensionsSupport.class)
public class GuiceBundleTest {

  private static final DropwizardAppExtension<Configuration> EXT =
    new DropwizardAppExtension<>(
      TestApplication.class,
      ResourceHelpers.resourceFilePath("test-config.yml")
    );

  private Environment environment;
  private GuiceBundle<Configuration> guiceBundle;

  @BeforeEach
  public void setup() {
    TestApplication testApplication = EXT.getApplication();
    this.guiceBundle = testApplication.getGuiceBundle();
    this.environment = EXT.getEnvironment();
  }

  @Test
  public void createsInjectorWhenInit() throws ServletException {
    Injector injector = guiceBundle.getInjector();
    assertThat(injector).isNotNull();
  }

  @Test
  public void serviceLocatorIsAvailable() throws ServletException {
    InjectionManager injectionManager = guiceBundle
      .getInjector()
      .getInstance(InjectionManager.class);
    assertThat(injectionManager).isNotNull();
  }

  @Test
  public void itAddsBoundManaged() {
    InjectedManaged injectedManaged = guiceBundle
      .getInjector()
      .getInstance(InjectedManaged.class);
    assertThat(environment.lifecycle().getManagedObjects())
      .extracting("managed")
      .containsOnlyOnce(injectedManaged);
  }

  @Test
  public void itAddsInstanceManaged() {
    InstanceManaged instanceManaged = guiceBundle
      .getInjector()
      .getInstance(InstanceManaged.class);
    assertThat(environment.lifecycle().getManagedObjects())
      .extracting("managed")
      .containsOnlyOnce(instanceManaged);
  }

  @Test
  public void itAddsProviderManagedSingleton() {
    ProviderManaged providerManaged = guiceBundle
      .getInjector()
      .getInstance(ProviderManaged.class);
    assertThat(environment.lifecycle().getManagedObjects())
      .extracting("managed")
      .containsOnlyOnce(providerManaged);
  }

  @Test
  public void itAddsBoundTask() {
    InjectedTask injectedTask = guiceBundle.getInjector().getInstance(InjectedTask.class);
    assertThat(environment.admin())
      .extracting("tasks")
      .extracting("tasks", as(InstanceOfAssertFactories.ITERABLE))
      .containsOnlyOnce(injectedTask);
  }

  @Test
  public void itAddsBoundHealthCheck() {
    assertThat(environment.healthChecks().getNames())
      .containsOnlyOnce(InjectedHealthCheck.class.getSimpleName());
  }

  @Test
  public void itAddsBoundServerLifecycleListener() {
    InjectedServerLifecycleListener injectedServerLifecycleListener = guiceBundle
      .getInjector()
      .getInstance(InjectedServerLifecycleListener.class);
    assertThat(environment.lifecycle())
      .extracting("lifecycleListeners", as(InstanceOfAssertFactories.ITERABLE))
      .extracting("listener")
      .containsOnlyOnce(injectedServerLifecycleListener);
  }

  @Test
  public void itAddsBoundProvider() {
    Set<Class<?>> components = environment.jersey().getResourceConfig().getClasses();
    assertThat(components).containsOnlyOnce(InjectedProvider.class);
  }

  @Test
  public void itAddsBoundResource() {
    Set<Class<?>> resourceClasses = environment.jersey().getResourceConfig().getClasses();
    assertThat(resourceClasses).containsOnlyOnce(ExplicitResource.class);
  }

  @Test
  public void itAddsProvidedManaged() {
    ProvidedManaged providedManaged = guiceBundle
      .getInjector()
      .getInstance(ProvidedManaged.class);
    assertThat(environment.lifecycle().getManagedObjects())
      .extracting("managed")
      .containsOnlyOnce(providedManaged);
  }

  @Test
  public void itAddsProvidedTask() {
    ProvidedTask providedTask = guiceBundle.getInjector().getInstance(ProvidedTask.class);
    assertThat(environment.admin())
      .extracting("tasks")
      .extracting("tasks", as(InstanceOfAssertFactories.ITERABLE))
      .containsOnlyOnce(providedTask);
  }

  @Test
  public void itAddsProvidedHealthCheck() {
    assertThat(environment.healthChecks().getNames())
      .containsOnlyOnce(ProvidedHealthCheck.class.getSimpleName());
  }

  @Test
  public void itAddsProvidedServerLifecycleListener() {
    ProvidedServerLifecycleListener providedServerLifecycleListener = guiceBundle
      .getInjector()
      .getInstance(ProvidedServerLifecycleListener.class);
    assertThat(environment.lifecycle())
      .extracting("lifecycleListeners", as(InstanceOfAssertFactories.ITERABLE))
      .extracting("listener")
      .containsOnlyOnce(providedServerLifecycleListener);
  }

  @Test
  public void itAddsProvidedProvider() {
    Set<Class<?>> components = environment.jersey().getResourceConfig().getClasses();
    assertThat(components).containsOnlyOnce(ProvidedProvider.class);
  }
}
