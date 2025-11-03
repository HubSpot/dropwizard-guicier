package com.hubspot.dropwizard.guicier.objects;

import io.dropwizard.servlets.tasks.Task;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ProvidedTask extends Task {

  public ProvidedTask(String name) {
    super(name);
  }

  @Override
  public void execute(Map<String, List<String>> parameters, PrintWriter output)
    throws Exception {}
}
