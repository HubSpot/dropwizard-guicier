package com.hubspot.dropwizard.guicier.objects;

import javax.ws.rs.ext.Provider;

import com.google.inject.Inject;

@Provider
public class InjectedProvider {

  @Inject
  InjectedProvider() {}
}
