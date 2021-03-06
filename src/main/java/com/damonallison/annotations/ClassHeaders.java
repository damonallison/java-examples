package com.damonallison.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ClassHeaders is a container annotation for the repeatable {@link ClassHeader}
 * annotation.
 * <p>
 * Each container annotation must have a single {@code value()} element. The
 * array's type must be the repeatable annotation type.
 * <p>
 * Container annotations like this are a complete hack. Each {@code Repeatable}
 * annotation must have it's own strongly typed container. This creates a lot of
 * boilerplate annotations!
 * <p>
 * NOTE : Repeatable annotations are supported in Java 1.8 and higher.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassHeaders {
    ClassHeader[] value();
}
