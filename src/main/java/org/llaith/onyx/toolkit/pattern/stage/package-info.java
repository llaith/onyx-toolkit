/*
 * Copyright (c) 2016.
 */

/**
 * Implementation of a execution flow of nested command-pattern influenced stages.
 * Has mechanisms for a controlled cleanup after failures. Used primarily for
 * batch processes.
 *
 * @author Nos Doughty
 * @version 2.0
 */
@Maturity(BETA)
package org.llaith.onyx.toolkit.pattern.stage;

import org.llaith.onyx.toolkit.etc.marker.Maturity;

import static org.llaith.onyx.toolkit.etc.marker.Maturity.MaturityLevel.BETA;