/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

/**
 * Used to wrap a impl specific enum into an api-level one. Very rare use case,
 * because usually it'd take more than changing an enum to switch impls, but
 * does happen occasionally.
 */
public interface EnumWrapper<E extends Enum<E>> {

    E toEnum();

}

