package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import javax.ws.rs.ext.Provider;

@Provider
public class InjectedProvider {

  @Inject
  InjectedProvider() {}
}
