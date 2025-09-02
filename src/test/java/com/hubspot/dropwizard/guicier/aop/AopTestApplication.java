package com.hubspot.dropwizard.guicier.aop;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.hubspot.dropwizard.guicier.GuiceBundle;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class AopTestApplication extends Application<Configuration> {

  private MyInterceptor interceptor = new MyInterceptor();

  public MyInterceptor getInterceptor() {
    return interceptor;
  }

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    bootstrap.addBundle(
      GuiceBundle
        .defaultBuilder(Configuration.class)
        .modules(
          new AbstractModule() {
            @Override
            protected void configure() {
              bind(MyResource.class);

              bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(MyAnnotation.class),
                interceptor
              );
            }
          }
        )
        .build()
    );
  }

  @Override
  public void run(Configuration configuration, Environment environment)
    throws Exception {}
}
