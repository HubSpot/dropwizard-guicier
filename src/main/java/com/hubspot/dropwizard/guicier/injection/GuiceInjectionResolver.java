package com.hubspot.dropwizard.guicier.injection;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.reflect.Type;
import java.util.Optional;
import javax.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injectee;
import org.glassfish.jersey.internal.inject.InjectionResolver;

@Singleton
public class GuiceInjectionResolver implements InjectionResolver<Inject> {

  private final Injector injector;
  private final ServiceLocator serviceLocator;

  public GuiceInjectionResolver(Injector injector, ServiceLocator serviceLocator) {
    this.injector = injector;
    this.serviceLocator = serviceLocator;
  }

  @Override
  public Object resolve(Injectee injectee) {
    Key<?> key = BindingUtils.toKey(injectee);

    Object instance = null;
    try {
      instance = injector.getInstance(key);
    } catch (ConfigurationException e) {
      /* fallback to jersey hk2 injector */
    }

    if (instance == null) {
      Optional<Type> translatedGuiceProviderType =
        BindingUtils.translateGuiceProviderType(injectee);

      instance =
        serviceLocator.getService(
          translatedGuiceProviderType.orElse(injectee.getRequiredType())
        );

      if (instance != null && translatedGuiceProviderType.isPresent()) {
        return BindingUtils.javaxToGuiceProvider(instance);
      }
    }

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
