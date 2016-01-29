# dropwizard-guicier

A Dropwizard bundle to handle Guice integration.

## Usage
```xml
    <dependencies>
        <dependency>
            <groupId>com.hubspot.dropwizard</groupId>
            <artifactId>dropwizard-guicier</artifactId>
            <version>0.9.1.0</version>
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
binding, [for example](https://github.com/jhaber/dropwizard-guicier-example/blob/b050d7487b6ab34d351b57a0dde4f0267f0c745e/src/main/java/com/hubspot/dropwizard/example/ExampleModule.java#L21-L24)
- Any `Managed`, `Task`, `HealthCheck`, or `ServerLifecycleListener` bound in Guice will be added to Dropwizard for you, [for example](https://github.com/jhaber/dropwizard-guicier-example/blob/b050d7487b6ab34d351b57a0dde4f0267f0c745e/src/main/java/com/hubspot/dropwizard/example/ExampleModule.java#L32-L38) (must be eager singletons for this to work)

## Examples
There is an [example project](https://github.com/jhaber/dropwizard-guicier-example) you can clone and play with if you'd like to get
going right away. 

Enjoy!
