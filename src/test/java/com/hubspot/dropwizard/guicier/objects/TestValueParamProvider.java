package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.function.Function;
import org.glassfish.jersey.model.Parameter.Source;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

public class TestValueParamProvider extends AbstractValueParamProvider {

  @Inject
  TestValueParamProvider(
    Provider<MultivaluedParameterExtractorProvider> extractorProvider
  ) {
    super(extractorProvider, Source.UNKNOWN);
  }

  @Override
  protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
    if (!parameter.isAnnotationPresent(TestValueParam.class)) {
      return null;
    }

    return request -> "testparam";
  }
}
