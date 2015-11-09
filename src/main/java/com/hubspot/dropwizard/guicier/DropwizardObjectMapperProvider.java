package com.hubspot.dropwizard.guicier;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;

import io.dropwizard.setup.Environment;

public class DropwizardObjectMapperProvider implements Provider<ObjectMapper> {
  private final Environment environment;

  @Inject
  public DropwizardObjectMapperProvider(final Environment environment) {
    this.environment = checkNotNull(environment, "environment is null");
  }

  @Override
  public ObjectMapper get() {
    return environment.getObjectMapper().copy();
  }
}
