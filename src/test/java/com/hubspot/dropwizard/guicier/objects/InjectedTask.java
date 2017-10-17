package com.hubspot.dropwizard.guicier.objects;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import io.dropwizard.servlets.tasks.Task;

@Singleton
public class InjectedTask extends Task {

    @Inject
    protected InjectedTask(@Named("TestTaskName") String name) {
        super(name);
    }

    @Override
    public void execute(ImmutableMultimap<String, String> immutableMultimap, PrintWriter printWriter) throws Exception {

    }
}
