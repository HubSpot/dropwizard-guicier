package com.hubspot.dropwizard.guicier.objects;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

@Provider
public class TestValueParamFeature implements Feature {

  @Inject
  public TestValueParamFeature() {}

  @Override
  public boolean configure(FeatureContext context) {
    context.register(
      new AbstractBinder() {
        @Override
        protected void configure() {
          bind(TestValueParamProvider.class)
            .to(ValueParamProvider.class)
            .in(Singleton.class);
        }
      }
    );
    return true;
  }
}
