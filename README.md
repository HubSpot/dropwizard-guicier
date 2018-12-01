# dropwizard-guicier

A Dropwizard bundle to handle Guice integration.

## Usage
```xml
    <dependencies>
        <dependency>
            <groupId>com.hubspot.dropwizard</groupId>
            <artifactId>dropwizard-guicier</artifactId>
            <version>1.3.5.0</version>
        </dependency>
    </dependencies>
```

Simply install a new instance of the bundle during your service initialization
```java
public class ExampleApplication extends Application<ExampleConfiguration> {

  public static void main(String... args) throws Exception {
    new ExampleApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
    GuiceBundle<ExampleConfiguration> guiceBundle = GuiceBundle.defaultBuilder(ExampleConfiguration.class)
        .modules(new ExampleModule())
        .build();

    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(ExampleConfiguration configuration, Environment environment) throws Exception {}
}
```

## Features
- Injector is created during the run phase so `Configuration` and `Environment` are available to eager singletons (injector is also
created with `Stage.PRODUCTION` by default) 
- Modules added to the `GuiceBundle` can extend `DropwizardAwareModule` which gives them
access to the `Bootstrap`, `Configuration`, and `Environment` inside of the `configure` method. This can be used to do conditional
binding, [for example](https://github.com/jhaber/dropwizard-guicier-example/blob/6a7aaaad8a69b3e3331504ebdf77754eccb9bf6b/src/main/java/com/hubspot/dropwizard/example/ExampleModule.java#L20-L23)
- Any `Managed`, `Task`, `HealthCheck`, or `ServerLifecycleListener` bound in Guice will be added to Dropwizard for you, [for example](https://github.com/jhaber/dropwizard-guicier-example/blob/6a7aaaad8a69b3e3331504ebdf77754eccb9bf6b/src/main/java/com/hubspot/dropwizard/example/ExampleModule.java#L31-L37) (must be eager singletons for this to work)

## Examples
There is an [example project](https://github.com/jhaber/dropwizard-guicier-example) you can clone and play with if you'd like to get
going right away. 

## Upgrading from dropwizard-guice
There are a couple important changes to be aware of when upgrading from [dropwizard-guice](https://github.com/HubSpot/dropwizard-guice).

### AutoConfig has been removed
[Reasoning and potential workarounds here](https://github.com/HubSpot/dropwizard-guicier/issues/41). The workarounds are however, not recommended.

### Explicit Bindings Required
All objects that you wish to inject must be explicitly bound. Just-in-time bindings are not supported by default. This is the case despite you **not** enabling explicit binding in your own module.

You can however (albeit not recommended) disable the explicit binding behavior by setting `enableGuiceEnforcer(false)` on the `GuiceBundle` within your DW application.

Enjoy!
