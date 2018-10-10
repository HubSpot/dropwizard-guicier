package com.hubspot.dropwizard.guicier.objects;

import io.dropwizard.lifecycle.ServerLifecycleListener;
import org.eclipse.jetty.server.Server;

public class ProvidedServerLifecycleListener implements ServerLifecycleListener {

  @Override
  public void serverStarted(Server server) {

  }
}
