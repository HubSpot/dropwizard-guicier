package com.hubspot.dropwizard.guicier.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Qualifier;

import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;
import org.glassfish.hk2.utilities.reflection.ReflectionHelper;
import org.glassfish.jersey.internal.inject.Injectee;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.internal.Nullability;

public class BindingUtils {

  public static Optional<Type> translateGuiceProviderType(Injectee injectee) {
    Type requiredType = injectee.getRequiredType();
    if (requiredType instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) requiredType;
      if (parameterizedType.getRawType().equals(Provider.class)) {
        return Optional.of(
          new ParameterizedTypeImpl(
            javax.inject.Provider.class,
            parameterizedType.getActualTypeArguments()
          )
        );
      }
    }
    return Optional.empty();
  }

  public static Provider<?> javaxToGuiceProvider(Object instance) {
    if (!(instance instanceof javax.inject.Provider)) {
      throw new IllegalArgumentException(
        "Given instance is not of type javax.inject.Provider: " + instance
      );
    }

    javax.inject.Provider<?> provider = (javax.inject.Provider<?>) instance;
    return provider::get;
  }

  /**
   * Returns {@code true} if the given {@link Injectee} can be {@code null}.
   *
   * @see Optional
   * @see Injectee#isOptional()
   * @see Nullable
   * @see com.google.inject.Inject#optional()
   * @see Nullability#hasNullableAnnotation(Annotation[])
   */
  public static boolean isNullable(Injectee injectee) {
    // HK2's optional
    if (injectee.isOptional()) {
      return true;
    }

    // Guice's optional
    AnnotatedElement element = injectee.getParent();
    if (isGuiceOptional(element)) {
      return true;
    }

    // Any @Nullable?
    int position = injectee.getPosition();

    if (element instanceof Field) {
      return Nullability.hasNullableAnnotation(((Field) element).getAnnotations());
    } else if (element instanceof Method) {
      Annotation annotations[][] = ((Method) element).getParameterAnnotations();
      return Nullability.hasNullableAnnotation(annotations[position]);
    } else if (element instanceof Constructor<?>) {
      Annotation annotations[][] = ((Constructor<?>) element).getParameterAnnotations();
      return Nullability.hasNullableAnnotation(annotations[position]);
    }

    return false;
  }

  /**
   * Returns {@code true} if the given {@link AnnotatedElement} has a
   * {@link com.google.inject.Inject} {@link Annotation} and it's marked
   * as being optional.
   *
   * @see com.google.inject.Inject#optional()
   */
  private static boolean isGuiceOptional(AnnotatedElement element) {
    com.google.inject.Inject inject = element.getAnnotation(
      com.google.inject.Inject.class
    );

    if (inject != null) {
      return inject.optional();
    }

    return false;
  }

  /**
   * Returns {@code true} if the {@link Injectee} has a HK2 SPI
   * {@link org.jvnet.hk2.annotations.Contract} annotation.
   *
   * @see org.jvnet.hk2.annotations.Contract
   */
  public static boolean isHk2Contract(Injectee injectee) {
    Type type = injectee.getRequiredType();
    return hasTypeAnnotation(type, org.jvnet.hk2.annotations.Contract.class);
  }

  /**
   * Returns {@code true} if the {@link Injectee} has a Jersey SPI
   * {@link org.glassfish.jersey.spi.Contract} annotation.
   *
   * @see org.glassfish.jersey.spi.Contract
   */
  public static boolean isJerseyContract(Injectee injectee) {
    Type type = injectee.getRequiredType();
    return hasTypeAnnotation(type, org.glassfish.jersey.spi.Contract.class);
  }

  public static boolean hasTypeAnnotation(
    Type type,
    Class<? extends Annotation> annotationType
  ) {
    if (type instanceof Class<?>) {
      return ((Class<?>) type).isAnnotationPresent(annotationType);
    }

    if (type instanceof ParameterizedType) {
      Type rawType = ((ParameterizedType) type).getRawType();
      return hasTypeAnnotation(rawType, annotationType);
    }

    return false;
  }

  /**
   * Creates and returns a {@link Key} from the given {@link Injectee}.
   */
  public static Key<?> toKey(Injectee injectee) {
    Type type = injectee.getRequiredType();
    Set<Annotation> qualifiers = getQualifiers(injectee);
    return newKey(type, qualifiers);
  }

  /**
   * Creates and returns a {@link Key} for the given {@link Type} and {@link Set} of {@link Annotation}s.
   */
  public static Key<?> newKey(Type type, Set<? extends Annotation> qualifiers) {
    if (qualifiers.isEmpty()) {
      return Key.get(type);
    }

    // There can be only one qualifier.
    if (qualifiers.size() == 1) {
      for (Annotation first : qualifiers) {
        return Key.get(type, first);
      }
    }

    return null;
  }

  /**
   * NOTE: There can be only one {@link Annotation} that is a {@link Qualifier} or {@link BindingAnnotation}.
   * They're the same but HK2 does not know about {@link BindingAnnotation}.
   *
   * @see Qualifier
   * @see BindingAnnotation
   * @see javax.inject.Named
   * @see com.google.inject.name.Named
   */
  private static Set<Annotation> getQualifiers(Injectee injectee) {
    // JSR 330's @Qualifier
    Set<Annotation> qualifiers = injectee.getRequiredQualifiers();
    if (!qualifiers.isEmpty()) {
      return qualifiers;
    }

    AnnotatedElement element = injectee.getParent();
    int position = injectee.getPosition();

    // Guice's @BindingAnnotation is the same as @Qualifier
    Annotation annotation = getBindingAnnotation(element, position);
    if (annotation != null) {
      return Collections.singleton(annotation);
    }

    return Collections.emptySet();
  }

  /**
   * Returns a {@link BindingAnnotation} for the given {@link AnnotatedElement} and position.
   */
  private static Annotation getBindingAnnotation(AnnotatedElement element, int position) {
    if (element instanceof Field) {
      return getBindingAnnotation(((Field) element).getAnnotations());
    }

    if (element instanceof Method) {
      Annotation annotations[][] = ((Method) element).getParameterAnnotations();
      return getBindingAnnotation(annotations[position]);
    }

    if (element instanceof Constructor<?>) {
      Annotation annotations[][] = ((Constructor<?>) element).getParameterAnnotations();
      return getBindingAnnotation(annotations[position]);
    }

    return null;
  }

  /**
   * Returns the first {@link Annotation} from the given array that
   * is a {@link BindingAnnotation}.
   *
   * @see BindingAnnotation
   */
  private static Annotation getBindingAnnotation(Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (type.isAnnotationPresent(BindingAnnotation.class)) {
        return annotation;
      }
    }

    return null;
  }

  /**
   * @see ReflectionHelper#getNameFromAllQualifiers(Set, AnnotatedElement)
   */
  public static String getNameFromAllQualifiers(
    Set<Annotation> qualifiers,
    AnnotatedElement element
  ) {
    return ReflectionHelper.getNameFromAllQualifiers(qualifiers, element);
  }
}
