package com.hubspot.dropwizard.guicier;

import java.util.Collection;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;

public interface InjectorFactory {
  Injector create(Stage stage, Module module);

  default Injector create(Stage stage, Collection<Module> modules) {
    return create(stage, Modules.combine(modules));
  }
}
