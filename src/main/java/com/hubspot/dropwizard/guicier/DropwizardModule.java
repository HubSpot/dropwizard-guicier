package com.hubspot.dropwizard.guicier;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
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
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

public class DropwizardModule implements Module {
  private static final Logger LOG = LoggerFactory.getLogger(DropwizardModule.class);

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

  public void register(Environment environment, Injector injector) {
    registerResourcesAndProviders(environment.jersey().getResourceConfig(), injector);

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

  private void registerResourcesAndProviders(ResourceConfig config, Injector injector) {
    while (injector != null) {
      for (Key<?> key : injector.getBindings().keySet()) {
        Type type = key.getTypeLiteral().getType();
        if (type instanceof Class<?>) {
          Class<?> c = (Class<?>) type;
          if (isProviderClass(c)) {
            LOG.info("Registering {} as a provider class", c.getName());
            config.register(c);
          } else if (isResourceClass(c)) {
            // Jersey rejects resources that it doesn't think are acceptable
            // Including abstract classes and interfaces, even if there is a valid Guice binding.
            if(Resource.isAcceptable(c)) {
              LOG.info("Registering {} as a root resource class", c.getName());
              config.register(c);
            } else {
              LOG.warn("Class {} was not registered as a resource; bind a concrete implementation instead", c.getName());
            }
          }

        }
      }
      injector = injector.getParent();
    }
  }

  private static boolean isProviderClass(Class<?> c) {
    return c.isAnnotationPresent(Provider.class);
  }

  private static boolean isResourceClass(Class<?> c) {
    if (c.isAnnotationPresent(Path.class)) {
      return true;
    }

    for (Class<?> i : c.getInterfaces()) {
      if (i.isAnnotationPresent(Path.class)) {
        return true;
      }
    }

    return false;
  }
}
