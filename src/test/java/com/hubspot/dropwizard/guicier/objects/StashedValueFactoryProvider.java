package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Provider;
import org.glassfish.jersey.model.Parameter.Source;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

public class StashedValueFactoryProvider extends AbstractValueParamProvider {

  private final Injector injector;

  @Inject
  public StashedValueFactoryProvider(
    Provider<MultivaluedParameterExtractorProvider> extractorProviderProvider,
    Injector injector
  ) {
    super(extractorProviderProvider, Source.UNKNOWN);
    this.injector = injector;
  }

  @Override
  protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
    if (!parameter.isAnnotationPresent(Stashed.class)) {
      return null;
    }

    return request -> injector.getInstance(Key.get(parameter.getType(), Stashed.class));
  }
}
