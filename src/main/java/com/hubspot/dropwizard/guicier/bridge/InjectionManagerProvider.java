package com.hubspot.dropwizard.guicier.bridge;

import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import javax.inject.Provider;
import org.glassfish.jersey.internal.inject.InjectionManager;

public class InjectionManagerProvider implements Provider<InjectionManager> {

  private static final AtomicReference<InjectionManager> INJECTION_MANAGER_REF =
    new AtomicReference<>();

  @Inject
  public InjectionManagerProvider() {}

  public static void set(InjectionManager injectionManager) {
    INJECTION_MANAGER_REF.set(injectionManager);
  }

  @Override
  public InjectionManager get() {
    return Preconditions.checkNotNull(
      INJECTION_MANAGER_REF.get(),
      "InjectionManager not set"
    );
  }
}
