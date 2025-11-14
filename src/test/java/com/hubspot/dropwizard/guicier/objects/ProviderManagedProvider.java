package com.hubspot.dropwizard.guicier.objects;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

public class ProviderManagedProvider implements Provider<ProviderManaged> {

  @Inject
  public ProviderManagedProvider() {}

  @Override
  public ProviderManaged get() {
    return new ProviderManaged();
  }
}
