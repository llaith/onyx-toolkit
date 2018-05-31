/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.jaxrs.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation used to discover endpoints for classpath scanning.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Resource {
}


