package com.hubspot.dropwizard.guicier.objects;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

public class ProvidedTask extends Task {

    public ProvidedTask(String name) {
        super(name);
    }

    @Override
    public void execute(ImmutableMultimap<String, String> immutableMultimap, PrintWriter printWriter) {

    }
}
