package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import javax.inject.Inject;
import javax.servlet.Servlet;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 */
public class GuiceBundle<T extends Configuration> implements ConfiguredBundle<T> {
  private static final String GUICE_BUNDLE_NAME = "_guice_bundle";
  private static final Named GUICE_BUNDLE_NAMED = Names.named(GUICE_BUNDLE_NAME);

  private static final Logger LOG = LoggerFactory.getLogger(GuiceBundle.class);

  public static <U extends Configuration> Builder<U> defaultBuilder(final Class<U> configClass) {
    return new Builder<>(configClass);
  }

  private final Class<T> configClass;
  private final ImmutableSet<DropwizardAwareModule<T>> dropwizardAwareModules;
  private final ImmutableSet<Module> guiceModules;
  private final Stage guiceStage;

  private Bootstrap<?> bootstrap = null;

  @Inject
  @Named(GUICE_BUNDLE_NAME)
  private volatile Function<ResourceConfig, Servlet> replacer = null;

  private GuiceBundle(final Class<T> configClass,
                      final ImmutableSet<Module> guiceModules,
                      final ImmutableSet<DropwizardAwareModule<T>> dropwizardAwareModules,
                      final Stage guiceStage) {
    this.configClass = configClass;

    this.guiceModules = guiceModules;
    this.dropwizardAwareModules = dropwizardAwareModules;
    this.guiceStage = guiceStage;
  }

  @Override
  public void initialize(final Bootstrap<?> bootstrap) {
    this.bootstrap = bootstrap;
  }

  @Override
  public void run(final T configuration, final Environment environment) throws Exception {
    for (DropwizardAwareModule<T> dropwizardAwareModule : dropwizardAwareModules) {
      dropwizardAwareModule.setBootstrap(bootstrap);
      dropwizardAwareModule.setConfiguration(configuration);
      dropwizardAwareModule.setEnvironment(environment);
    }

    final DropwizardModule dropwizardModule = new DropwizardModule();

    final Injector injector =
        Guice.createInjector(guiceStage,
                ImmutableSet.<Module>builder()
                        .addAll(guiceModules)
                        .addAll(dropwizardAwareModules)
                        .add(new GuiceEnforcerModule())
                        .add(new JerseyServletModule())
                        .add(dropwizardModule)
                        .add(new Module() {
                          @Override
                          public void configure(final Binder binder) {
                            binder.bind(Environment.class).toInstance(environment);
                            binder.bind(configClass).toInstance(configuration);

                            binder.bind(ServletContainer.class).to(DropwizardGuiceContainer.class);

                            binder.bind(new TypeLiteral<Function<ResourceConfig, Servlet>>() {
                            })
                                    .annotatedWith(GUICE_BUNDLE_NAMED)
                                    .to(GuiceContainerReplacer.class)
                                    .in(Scopes.SINGLETON);
                          }
                        }).build());

    injector.injectMembers(this);
    checkState(replacer != null, "No guice container replacer was injected!");

    dropwizardModule.register(environment);

    environment.jersey().replace(replacer);
    environment.servlets().addFilter("Guice Filter", GuiceFilter.class).addMappingForUrlPatterns(null, false, environment.getApplicationContext().getContextPath() + "*");
  }

  private static class GuiceContainerReplacer implements Function<ResourceConfig, Servlet> {
    private final DropwizardGuiceContainer container;

    @Inject
    GuiceContainerReplacer(final DropwizardGuiceContainer container) {
      this.container = checkNotNull(container, "container is null");
    }

    @Override
    public Servlet apply(final ResourceConfig resourceConfig) {
      return container;
    }
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
      return new GuiceBundle<U>(configClass, guiceModules.build(), dropwizardAwareModules.build(), guiceStage);
    }
  }
}
