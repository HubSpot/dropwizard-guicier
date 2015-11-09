package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 */
public abstract class ConfigurationAwareModule<Configuration> implements Module {
  private volatile Configuration configuration = null;

  @Override
  public final void configure(Binder binder) {
    configure(decorate(binder), getConfiguration());
  }

  public void setConfiguration(Configuration configuration) {
    checkState(this.configuration == null, "configuration was already set!");
    this.configuration = checkNotNull(configuration, "configuration is null");
  }

  protected Configuration getConfiguration() {
    return checkNotNull(this.configuration, "configuration was not set!");
  }

  protected abstract void configure(final Binder binder, final Configuration configuration);

  private Binder decorate(final Binder binder) {
    return new ForwardingBinder() {

      @Override
      protected Binder getDelegate() {
        return binder;
      }

      @Override
      @SuppressWarnings("unchecked")
      public void install(Module module) {
        if (module instanceof ConfigurationAwareModule<?>) {
          ((ConfigurationAwareModule<Configuration>) module).setConfiguration(getConfiguration());
        }

        super.install(module);
      }
    };
  }
}
