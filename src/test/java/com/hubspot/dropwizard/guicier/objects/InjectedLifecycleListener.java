package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.jetty.util.component.LifeCycle;

@Singleton
public class InjectedLifecycleListener implements LifeCycle.Listener {

  @Inject
  InjectedLifecycleListener() {}

  @Override
  public void lifeCycleStarting(LifeCycle lifeCycle) {
  }

  @Override
  public void lifeCycleStarted(LifeCycle lifeCycle) {
  }

  @Override
  public void lifeCycleFailure(LifeCycle lifeCycle, Throwable throwable) {
  }

  @Override
  public void lifeCycleStopping(LifeCycle lifeCycle) {
  }

  @Override
  public void lifeCycleStopped(LifeCycle lifeCycle) {
  }
}
