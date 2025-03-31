package com.hubspot.dropwizard.guicier.injection;

import static com.hubspot.dropwizard.guicier.injection.BindingUtils.newKey;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import org.glassfish.jersey.internal.inject.InjectionManager;

/**
 * InjectionManager which gets instances from Guice bindings first, falling back to delegate
 */
public class BridgedGuiceInjectionManager extends DelegatingInjectionManager {

  private final Injector guiceInjector;

  public BridgedGuiceInjectionManager(InjectionManager delegate, Injector guiceInjector) {
    super(delegate);
    this.guiceInjector = guiceInjector;
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl, Annotation... qualifiers) {
    T guiceInstance = getGuiceInstance(newKey(contractOrImpl, Set.of(qualifiers)));
    return guiceInstance != null
      ? guiceInstance
      : super.getInstance(contractOrImpl, qualifiers);
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl, String classAnalyzer) {
    T guiceInstance = getGuiceInstance(newKey(contractOrImpl, Set.of()));
    return guiceInstance != null
      ? guiceInstance
      : super.getInstance(contractOrImpl, classAnalyzer);
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl) {
    T guiceInstance = getGuiceInstance(newKey(contractOrImpl, Set.of()));
    return guiceInstance != null ? guiceInstance : super.getInstance(contractOrImpl);
  }

  @Override
  public <T> T getInstance(Type contractOrImpl) {
    T guiceInstance = getGuiceInstance(newKey(contractOrImpl, Set.of()));
    return guiceInstance != null ? guiceInstance : super.getInstance(contractOrImpl);
  }

  @SuppressWarnings("unchecked")
  private <T> T getGuiceInstance(Key<?> key) {
    if (guiceInjector.getExistingBinding(key) == null) {
      return null;
    }

    T instance = (T) guiceInjector.getInstance(key);
    if (instance != null) {
      super.inject(instance);
    }
    return instance;
  }
}
