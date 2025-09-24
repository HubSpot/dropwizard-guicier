package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import org.eclipse.jetty.server.Server;

@Singleton
public class InjectedServerLifecycleListener implements ServerLifecycleListener {

  @Inject
  InjectedServerLifecycleListener() {}

  @Override
  public void serverStarted(Server server) {}
}
