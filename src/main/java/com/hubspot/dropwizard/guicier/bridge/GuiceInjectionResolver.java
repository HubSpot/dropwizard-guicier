package com.hubspot.dropwizard.guicier.bridge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import javax.inject.Singleton;
import org.glassfish.jersey.internal.inject.Injectee;
import org.glassfish.jersey.internal.inject.InjectionResolver;

@Singleton
public class GuiceInjectionResolver implements InjectionResolver<Inject> {

  private final Injector injector;

  public GuiceInjectionResolver(Injector injector) {
    this.injector = injector;
  }

  @Override
  public Object resolve(Injectee injectee) {
    Key<?> key = BindingUtils.toKey(injectee);

    Object instance = injector.getInstance(key);

    if (instance == null) {
      if (BindingUtils.isNullable(injectee)) {
        return null;
      }

      throw new RuntimeException(
        "There was no object available for injection at " + injectee
      );
    }

    return instance;
  }

  @Override
  public boolean isConstructorParameterIndicator() {
    return true;
  }

  @Override
  public boolean isMethodParameterIndicator() {
    return false;
  }

  @Override
  public Class<Inject> getAnnotation() {
    return Inject.class;
  }
}
