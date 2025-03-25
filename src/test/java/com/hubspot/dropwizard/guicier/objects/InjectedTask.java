package com.hubspot.dropwizard.guicier.objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.dropwizard.servlets.tasks.Task;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Singleton
public class InjectedTask extends Task {

  @Inject
  protected InjectedTask(@Named("TestTaskName") String name) {
    super(name);
  }

  @Override
  public void execute(Map<String, List<String>> parameters, PrintWriter output)
    throws Exception {}
}
