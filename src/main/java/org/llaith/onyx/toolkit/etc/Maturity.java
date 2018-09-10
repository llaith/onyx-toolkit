/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD})
public @interface Maturity {

    MaturityLevel value();

    /**
     *
     */
    enum MaturityLevel {

        EXPERIMENT, // completely untested. An idea I am quickly jotting down for later testing.
        ALPHA, // minimally tested. Not yet used in an application.
        BETA, // used in at least one application where it appears to function. The 'good' case likely has no bugs.
        RELEASE, // properly tested and safe to use. The 'error' cases are expected to be bug free.
        STABLE // safe to use. Has been in use in multiple applications and has evolved to be useful in general cases.

    }
}
