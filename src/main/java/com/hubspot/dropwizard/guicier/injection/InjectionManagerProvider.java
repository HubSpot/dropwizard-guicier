package com.hubspot.dropwizard.guicier.injection;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.setup.Environment;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.internal.inject.InjectionManager;

@Singleton
public class InjectionManagerProvider implements Provider<InjectionManager> {

  private static final Map<Application, InjectionManager> INJECTION_MANAGERS_BY_APP =
    new HashMap<>();

  private final Application application;

  @Inject
  public InjectionManagerProvider(Environment environment) {
    this.application = environment.jersey().getResourceConfig();
  }

  public static void set(Application application, InjectionManager injectionManager) {
    INJECTION_MANAGERS_BY_APP.put(application, injectionManager);
  }

  @Override
  public InjectionManager get() {
    return checkNotNull(
      INJECTION_MANAGERS_BY_APP.get(application),
      "InjectionManager not set"
    );
  }
}
