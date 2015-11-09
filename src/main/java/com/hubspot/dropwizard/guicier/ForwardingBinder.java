package com.hubspot.dropwizard.guicier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;

public abstract class ForwardingBinder implements Binder {

  protected abstract Binder getDelegate();

  @Override
  public void install(Module module) {
    getDelegate().install(module);
  }

  @Override
  public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                              Matcher<? super Method> methodMatcher,
                              MethodInterceptor... interceptors) {
    getDelegate().bindInterceptor(classMatcher, methodMatcher, interceptors);
  }

  @Override
  public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
    getDelegate().bindScope(annotationType, scope);
  }

  @Override
  public <T> LinkedBindingBuilder<T> bind(Key<T> key) {
    return getDelegate().bind(key);
  }

  @Override
  public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
    return getDelegate().bind(typeLiteral);
  }

  @Override
  public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
    return getDelegate().bind(type);
  }

  @Override
  public AnnotatedConstantBindingBuilder bindConstant() {
    return getDelegate().bindConstant();
  }

  @Override
  public <T> void requestInjection(TypeLiteral<T> type, T instance) {
    getDelegate().requestInjection(type, instance);
  }

  @Override
  public void requestInjection(Object instance) {
    getDelegate().requestInjection(instance);
  }

  @Override
  public void requestStaticInjection(Class<?>... types) {
    getDelegate().requestStaticInjection(types);
  }

  @Override
  public Stage currentStage() {
    return getDelegate().currentStage();
  }

  @Override
  public void addError(String message, Object... arguments) {
    getDelegate().addError(message, arguments);
  }

  @Override
  public void addError(Throwable t) {
    getDelegate().addError(t);
  }

  @Override
  public void addError(Message message) {
    getDelegate().addError(message);
  }

  @Override
  public <T> Provider<T> getProvider(Key<T> key) {
    return getDelegate().getProvider(key);
  }

  @Override
  public <T> Provider<T> getProvider(Dependency<T> dependency) {
    return getDelegate().getProvider(dependency);
  }

  @Override
  public <T> Provider<T> getProvider(Class<T> type) {
    return getDelegate().getProvider(type);
  }

  @Override
  public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
    return getDelegate().getMembersInjector(typeLiteral);
  }

  @Override
  public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
    return getDelegate().getMembersInjector(type);
  }

  @Override
  public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
    getDelegate().convertToTypes(typeMatcher, converter);
  }

  @Override
  public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
    getDelegate().bindListener(typeMatcher, listener);
  }

  @Override
  public void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
    getDelegate().bindListener(bindingMatcher, listeners);
  }

  @Override
  public Binder withSource(Object source) {
    return getDelegate().withSource(source);
  }

  @Override
  public Binder skipSources(Class... classesToSkip) {
    return getDelegate().skipSources(classesToSkip);
  }

  @Override
  public PrivateBinder newPrivateBinder() {
    return getDelegate().newPrivateBinder();
  }

  @Override
  public void requireExplicitBindings() {
    getDelegate().requireExplicitBindings();
  }

  @Override
  public void disableCircularProxies() {
    getDelegate().disableCircularProxies();
  }

  @Override
  public void requireAtInjectOnConstructors() {
    getDelegate().requireAtInjectOnConstructors();
  }

  @Override
  public void requireExactBindingAnnotations() {
    getDelegate().requireExactBindingAnnotations();
  }

  @Override
  public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {
    getDelegate().scanModulesForAnnotatedMethods(scanner);
  }
}
