package com.hubspot.dropwizard.guicier.injection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Singleton;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;
import org.glassfish.jersey.internal.inject.Injectee;
import org.glassfish.jersey.internal.inject.InjectionResolver;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

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
      Type t = injectee.getRequiredType();
      boolean wrapProvider = false;
      if (t instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) t;
        if (pt.getRawType().equals(Provider.class)) {
          t =
            new ParameterizedTypeImpl(
              javax.inject.Provider.class,
              pt.getActualTypeArguments()
            );
          wrapProvider = true;
        }
      }
      instance = serviceLocator.getService(t);
      if (instance != null && wrapProvider) {
        javax.inject.Provider jaxProvider = (javax.inject.Provider) instance;
        return (Provider) jaxProvider::get;
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
