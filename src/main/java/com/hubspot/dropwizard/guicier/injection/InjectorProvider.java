package com.hubspot.dropwizard.guicier.injection;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Injector;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Provider;

public class InjectorProvider implements Provider<Injector> {

  private static final AtomicReference<Injector> REF = new AtomicReference<>();

  public static void set(Injector injector) {
    REF.set(injector);
  }

  public static Optional<Injector> getMaybe() {
    return Optional.ofNullable(REF.get());
  }

  @Override
  public Injector get() {
    return checkNotNull(REF.get(), "Guice Injector not set");
  }
}
