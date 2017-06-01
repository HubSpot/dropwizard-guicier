package com.hubspot.dropwizard.guicier;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public interface InjectorFactory {
  Injector create(Stage stage, Module module);
}
