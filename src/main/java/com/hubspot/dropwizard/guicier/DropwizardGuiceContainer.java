package com.hubspot.dropwizard.guicier;

import io.dropwizard.setup.Environment;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 */
public class DropwizardGuiceContainer extends ServletContainer {

  public DropwizardGuiceContainer(final Environment environment) {
    super(environment.jersey().getResourceConfig());
  }
}
