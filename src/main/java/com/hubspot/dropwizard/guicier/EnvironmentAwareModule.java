package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Binder;
import com.google.inject.Module;

import io.dropwizard.setup.Environment;

public abstract class EnvironmentAwareModule implements Module {
  private volatile Environment environment = null;

  @Override
  public final void configure(Binder binder) {
    configure(decorate(binder), getEnvironment());
  }

  protected Environment getEnvironment() {
    return checkNotNull(this.environment, "environment was not set!");
  }

  public void setEnvironment(Environment environment) {
    checkState(this.environment == null, "environment was already set!");
    this.environment = checkNotNull(environment, "environment is null");
  }

  protected abstract void configure(Binder binder, Environment environment);

  private Binder decorate(final Binder binder) {
    return new ForwardingBinder() {

      @Override
      protected Binder getDelegate() {
        return binder;
      }

      @Override
      public void install(Module module) {
        if (module instanceof EnvironmentAwareModule) {
          ((EnvironmentAwareModule) module).setEnvironment(getEnvironment());
        }

        super.install(module);
      }
    };
  }
}
