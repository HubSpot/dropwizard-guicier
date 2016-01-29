package com.hubspot.dropwizard.guicier;

import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 */
@Singleton
public class DropwizardGuiceContainer extends ServletContainer {

  @Inject
  public DropwizardGuiceContainer(final Environment environment, final Injector injector) {
    super(environment.jersey().getResourceConfig());
  }
}
