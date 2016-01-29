package com.hubspot.dropwizard.guicier;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

public class DropwizardModule implements Module {
  private static final Logger LOG = LoggerFactory.getLogger(DropwizardModule.class);

  private final ImmutableSet.Builder<Object> resources = ImmutableSet.builder();
  private final ImmutableSet.Builder<Object> providers = ImmutableSet.builder();
  private final ImmutableSet.Builder<Managed> managedBuilder = ImmutableSet.builder();
  private final ImmutableSet.Builder<Task> taskBuilder = ImmutableSet.builder();
  private final ImmutableSet.Builder<HealthCheck> healthcheckBuilder = ImmutableSet.builder();
  private final ImmutableSet.Builder<ServerLifecycleListener> serverLifecycleListenerBuilder = ImmutableSet.builder();

  @Override
  public void configure(final Binder binder) {
    binder.bindListener(Matchers.any(), new TypeListener() {
      @Override
      public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        encounter.register(new InjectionListener<T>() {

          @Override
          public void afterInjection(T obj) {
            if (obj.getClass().isAnnotationPresent(Path.class)) {
              resources.add(obj);
            }

            if (obj.getClass().isAnnotationPresent(Provider.class)) {
              providers.add(obj);
            }

            if (obj instanceof Managed) {
              managedBuilder.add((Managed) obj);
            }

            if (obj instanceof Task) {
              taskBuilder.add((Task) obj);
            }

            if (obj instanceof HealthCheck) {
              healthcheckBuilder.add((HealthCheck) obj);
            }

            if (obj instanceof ServerLifecycleListener) {
              serverLifecycleListenerBuilder.add((ServerLifecycleListener) obj);
            }
          }
        });
      }
    });
  }

  public void register(Environment environment) {
    for (Object resource : resources.build()) {
      environment.jersey().getResourceConfig().register(resource);
      LOG.info("Added guice injected resource: {}", resource.getClass().getName());
    }

    for (Object provider : providers.build()) {
      environment.jersey().getResourceConfig().register(provider);
      LOG.info("Added guice injected provider: {}", provider.getClass().getName());
    }

    for (Managed managed : managedBuilder.build()) {
      environment.lifecycle().manage(managed);
      LOG.info("Added guice injected managed Object: {}", managed.getClass().getName());
    }

    for (Task task : taskBuilder.build()) {
      environment.admin().addTask(task);
      LOG.info("Added guice injected Task: {}", task.getClass().getName());
    }

    for (HealthCheck healthcheck : healthcheckBuilder.build()) {
      environment.healthChecks().register(healthcheck.getClass().getSimpleName(), healthcheck);
      LOG.info("Added guice injected health check: {}", healthcheck.getClass().getName());
    }

    for (ServerLifecycleListener serverLifecycleListener : serverLifecycleListenerBuilder.build()) {
      environment.lifecycle().addServerLifecycleListener(serverLifecycleListener);
      LOG.info("Added guice injected server lifecycle listener: {}", serverLifecycleListener.getClass().getName());
    }
  }
}
