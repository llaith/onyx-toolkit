/*
 * Copyright (c) 2016.
 */

/**
 * Faults are used to model warnings and errors during long running processes
 * that involved working on sets of resources that are excluded on such faults
 * rather than aborting the entire process.
 *
 * @author Nos Doughty
 * @version 1.0
 */
@Maturity(BETA)
package org.llaith.onyx.toolkit.pattern.fault;

import org.llaith.onyx.toolkit.etc.marker.Maturity;

import static org.llaith.onyx.toolkit.etc.marker.Maturity.MaturityLevel.BETA;