package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Binder;
import com.google.inject.Module;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class DropwizardAwareModule<C extends Configuration> implements Module {
  private volatile Bootstrap<?> bootstrap = null;
  private volatile C configuration = null;
  private volatile Environment environment = null;

  protected Bootstrap<?> getBootstrap() {
    return checkNotNull(this.bootstrap, "bootstrap was not set!");
  }

  protected C getConfiguration() {
    return checkNotNull(this.configuration, "configuration was not set!");
  }

  protected Environment getEnvironment() {
    return checkNotNull(this.environment, "environment was not set!");
  }

  public void setBootstrap(Bootstrap<?> bootstrap) {
    checkState(this.bootstrap == null, "bootstrap was already set!");
    this.bootstrap = checkNotNull(bootstrap, "bootstrap is null");
  }

  public void setConfiguration(C configuration) {
    checkState(this.configuration == null, "configuration was already set!");
    this.configuration = checkNotNull(configuration, "configuration is null");
  }

  public void setEnvironment(Environment environment) {
    checkState(this.environment == null, "environment was already set!");
    this.environment = checkNotNull(environment, "environment is null");
  }

  public void install(Binder binder, DropwizardAwareModule<Configuration> module) {
    module.setBootstrap(getBootstrap());
    module.setConfiguration(getConfiguration());
    module.setEnvironment(getEnvironment());
    binder.install(module);
  }
}
