package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.extension.ServiceLocatorGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 */
public class GuiceBundle<T extends Configuration> implements ConfiguredBundle<T> {
  private static final Logger LOG = LoggerFactory.getLogger(GuiceBundle.class);

  public static <U extends Configuration> Builder<U> defaultBuilder(final Class<U> configClass) {
    return new Builder<>(configClass);
  }

  private final Class<T> configClass;
  private final ImmutableSet<DropwizardAwareModule<T>> dropwizardAwareModules;
  private final ImmutableSet<Module> guiceModules;
  private final Stage guiceStage;
  private final boolean allowUnknownFields;
  private final boolean enableGuiceEnforcer;

  private Bootstrap<?> bootstrap = null;
  private Injector injector = null;

  private GuiceBundle(final Class<T> configClass,
                      final ImmutableSet<Module> guiceModules,
                      final ImmutableSet<DropwizardAwareModule<T>> dropwizardAwareModules,
                      final Stage guiceStage,
                      final boolean allowUnknownFields,
                      final boolean enableGuiceEnforcer) {
    this.configClass = configClass;

    this.guiceModules = guiceModules;
    this.dropwizardAwareModules = dropwizardAwareModules;
    this.guiceStage = guiceStage;
    this.allowUnknownFields = allowUnknownFields;
    this.enableGuiceEnforcer = enableGuiceEnforcer;
  }

  @Override
  public void initialize(final Bootstrap<?> bootstrap) {
    this.bootstrap = bootstrap;
    if (allowUnknownFields) {
      AllowUnknownFieldsObjectMapper.applyTo(bootstrap);
    }
  }

  @Override
  public void run(final T configuration, final Environment environment) throws Exception {
    for (DropwizardAwareModule<T> dropwizardAwareModule : dropwizardAwareModules) {
      dropwizardAwareModule.setBootstrap(bootstrap);
      dropwizardAwareModule.setConfiguration(configuration);
      dropwizardAwareModule.setEnvironment(environment);
    }

    final DropwizardModule dropwizardModule = new DropwizardModule(environment);

    ImmutableSet.Builder<Module> modulesBuilder =
        ImmutableSet.<Module>builder()
            .addAll(guiceModules)
            .addAll(dropwizardAwareModules)
            .add(new ServletModule())
            .add(dropwizardModule)
            .add(new Module() {
              @Override
              public void configure(final Binder binder) {
                binder.bind(Environment.class).toInstance(environment);
                binder.bind(configClass).toInstance(configuration);
              }
            });
    if (enableGuiceEnforcer) {
      modulesBuilder.add(new GuiceEnforcerModule());
    }
    this.injector = Guice.createInjector(guiceStage, modulesBuilder.build());

    JerseyGuiceUtils.install(new ServiceLocatorGenerator() {

      @Override
      public ServiceLocator create(String name, ServiceLocator parent) {
        if (!name.startsWith("__HK2_Generated_")) {
          return null;
        }

        return injector.createChildInjector(new JerseyGuiceModule(name)).getInstance(ServiceLocator.class);
      }
    });

    dropwizardModule.register(injector);

    environment.servlets().addFilter("Guice Filter", GuiceFilter.class).addMappingForUrlPatterns(null, false, "/*");
    environment.servlets().addServletListeners(new GuiceServletContextListener() {

      @Override
      protected Injector getInjector() {
        return injector;
      }
    });
  }

  public Injector getInjector() {
    return checkNotNull(injector, "injector has not been initialized yet");
  }

  public static class GuiceEnforcerModule implements Module {
    @Override
    public void configure(final Binder binder) {
      binder.disableCircularProxies();
      binder.requireExplicitBindings();
      binder.requireExactBindingAnnotations();
      binder.requireAtInjectOnConstructors();
    }
  }

  public static class Builder<U extends Configuration> {
    private final Class<U> configClass;
    private final ImmutableSet.Builder<Module> guiceModules = ImmutableSet.builder();
    private final ImmutableSet.Builder<DropwizardAwareModule<U>> dropwizardAwareModules = ImmutableSet.builder();
    private Stage guiceStage = Stage.PRODUCTION;
    private boolean allowUnknownFields = true;
    private boolean enableGuiceEnforcer = true;

    private Builder(final Class<U> configClass) {
      this.configClass = configClass;
    }

    public final Builder<U> stage(final Stage guiceStage) {
      checkNotNull(guiceStage, "guiceStage is null");
      if (guiceStage != Stage.PRODUCTION) {
        LOG.warn("Guice should only ever run in PRODUCTION mode except for testing!");
      }
      this.guiceStage = guiceStage;
      return this;
    }

    public final Builder<U> allowUnknownFields(final boolean allowUnknownFields) {
      this.allowUnknownFields = allowUnknownFields;
      return this;
    }

    public final Builder<U> enableGuiceEnforcer(final boolean enableGuiceEnforcer) {
      this.enableGuiceEnforcer = enableGuiceEnforcer;
      return this;
    }

    public final Builder<U> modules(final Module... modules) {
      return modules(Arrays.asList(modules));
    }

    @SuppressWarnings("unchecked")
    public final Builder<U> modules(final Iterable<? extends Module> modules) {
      for (Module module : modules) {
        if (module instanceof DropwizardAwareModule<?>) {
          dropwizardAwareModules.add((DropwizardAwareModule<U>) module);
        } else {
          guiceModules.add(module);
        }
      }
      return this;
    }

    public final GuiceBundle<U> build() {
      return new GuiceBundle<>(configClass,
                               guiceModules.build(),
                               dropwizardAwareModules.build(),
                               guiceStage,
                               allowUnknownFields,
                               enableGuiceEnforcer);
    }
  }
}
