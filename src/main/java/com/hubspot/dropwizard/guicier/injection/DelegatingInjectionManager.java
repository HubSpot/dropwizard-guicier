package com.hubspot.dropwizard.guicier.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import org.glassfish.jersey.internal.inject.Binder;
import org.glassfish.jersey.internal.inject.Binding;
import org.glassfish.jersey.internal.inject.ForeignDescriptor;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.ServiceHolder;

public class DelegatingInjectionManager implements InjectionManager {

  private final InjectionManager delegate;

  public DelegatingInjectionManager(InjectionManager delegate) {
    this.delegate = delegate;
  }

  @Override
  public void completeRegistration() {
    delegate.completeRegistration();
  }

  @Override
  public void shutdown() {
    delegate.shutdown();
  }

  @Override
  public boolean isShutdown() {
    return delegate.isShutdown();
  }

  @Override
  public void register(Binding binding) {
    delegate.register(binding);
  }

  @Override
  public void register(Iterable<Binding> descriptors) {
    delegate.register(descriptors);
  }

  @Override
  public void register(Binder binder) {
    delegate.register(binder);
  }

  @Override
  public void register(Object provider) throws IllegalArgumentException {
    delegate.register(provider);
  }

  @Override
  public boolean isRegistrable(Class<?> clazz) {
    return delegate.isRegistrable(clazz);
  }

  @Override
  public <T> T create(Class<T> createMe) {
    return delegate.create(createMe);
  }

  @Override
  public <T> T createAndInitialize(Class<T> createMe) {
    return delegate.createAndInitialize(createMe);
  }

  @Override
  public <T> List<ServiceHolder<T>> getAllServiceHolders(
    Class<T> contractOrImpl,
    Annotation... qualifiers
  ) {
    return delegate.getAllServiceHolders(contractOrImpl, qualifiers);
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl, Annotation... qualifiers) {
    return delegate.getInstance(contractOrImpl, qualifiers);
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl, String classAnalyzer) {
    return delegate.getInstance(contractOrImpl, classAnalyzer);
  }

  @Override
  public <T> T getInstance(Class<T> contractOrImpl) {
    return delegate.getInstance(contractOrImpl);
  }

  @Override
  public <T> T getInstance(Type contractOrImpl) {
    return delegate.getInstance(contractOrImpl);
  }

  @Override
  public Object getInstance(ForeignDescriptor foreignDescriptor) {
    return delegate.getInstance(foreignDescriptor);
  }

  @Override
  public ForeignDescriptor createForeignDescriptor(Binding binding) {
    return delegate.createForeignDescriptor(binding);
  }

  @Override
  public <T> List<T> getAllInstances(Type contractOrImpl) {
    return delegate.getAllInstances(contractOrImpl);
  }

  @Override
  public void inject(Object injectMe) {
    delegate.inject(injectMe);
  }

  @Override
  public void inject(Object injectMe, String classAnalyzer) {
    delegate.inject(injectMe, classAnalyzer);
  }

  @Override
  public void preDestroy(Object preDestroyMe) {
    delegate.preDestroy(preDestroyMe);
  }
}
