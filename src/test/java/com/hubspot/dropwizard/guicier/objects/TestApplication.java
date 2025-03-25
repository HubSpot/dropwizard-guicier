package com.hubspot.dropwizard.guicier.objects;

import com.hubspot.dropwizard.guicier.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<Configuration> {

  private GuiceBundle<Configuration> guiceBundle;

  public GuiceBundle<Configuration> getGuiceBundle() {
    return guiceBundle;
  }

  @Override
  public void initialize(final Bootstrap<Configuration> bootstrap) {
    this.guiceBundle =
      GuiceBundle.defaultBuilder(Configuration.class).modules(new TestModule()).build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(Configuration configuration, Environment environment)
    throws Exception {}
}
