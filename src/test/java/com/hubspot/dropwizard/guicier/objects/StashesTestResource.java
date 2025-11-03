package com.hubspot.dropwizard.guicier.objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/stashes")
@Produces(APPLICATION_JSON)
public class StashesTestResource {

  private final int stashedIntCtor;
  private final com.google.inject.Provider<Integer> stashedIntGuiceProviderCtor;
  private final javax.inject.Provider<Integer> stashedIntJavaxProviderCtor;

  @Inject
  @Stashed
  private int stashedIntField;

  @Inject
  @Stashed
  private com.google.inject.Provider<Integer> stashedIntGuiceProviderField;

  @Inject
  @Stashed
  private javax.inject.Provider<Integer> stashedIntJavaxProviderField;

  @Inject
  public StashesTestResource(
    @Stashed int stashedIntCtor,
    @Stashed com.google.inject.Provider<Integer> stashedIntGuiceProviderCtor,
    @Stashed javax.inject.Provider<Integer> stashedIntJavaxProviderCtor
  ) {
    this.stashedIntCtor = stashedIntCtor;
    this.stashedIntGuiceProviderCtor = stashedIntGuiceProviderCtor;
    this.stashedIntJavaxProviderCtor = stashedIntJavaxProviderCtor;
  }

  @GET
  @Path("/itConstructorInjectsAStashedInt")
  public int itConstructorInjectsAStashedInt() {
    return stashedIntCtor;
  }

  @GET
  @Path("/itConstructorInjectsAStashedIntGuiceProvider")
  public Integer itConstructorInjectsAStashedIntGuiceProvider() {
    return stashedIntGuiceProviderCtor.get();
  }

  @GET
  @Path("/itConstructorInjectsAStashedIntJavaxProvider")
  public Integer itConstructorInjectsAStashedIntJavaxProvider() {
    return stashedIntJavaxProviderCtor.get();
  }

  @GET
  @Path("/itFieldInjectsAStashedInt")
  public int itFieldInjectsAStashedInt() {
    return stashedIntField;
  }

  @GET
  @Path("/itFieldInjectsAStashedIntGuiceProvider")
  public Integer itFieldInjectsAStashedIntGuiceProvider() {
    return stashedIntGuiceProviderField.get();
  }

  @GET
  @Path("/itFieldInjectsAStashedIntJavaxProvider")
  public Integer itFieldInjectsAStashedIntJavaxProvider() {
    return stashedIntJavaxProviderField.get();
  }

  @GET
  @Path("/itMethodInjectsAStashedInt")
  public int itMethodInjectsAStashedInt(@Stashed int stashedInt) {
    return stashedInt;
  }
}
