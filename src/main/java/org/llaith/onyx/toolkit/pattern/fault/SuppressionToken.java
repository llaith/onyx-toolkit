/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.fault;

/**
 *
 * Metrics (stage timings are these)
 * Faults (do we record a FaultOccurance with wether it was supressed or not?)
 * Locations and stages should be nestedly queryable... hasFault(model) & hasFault(model.field), throwIfFault()
 *
 *
 * Use 'Metrics' library.
 * Have ExecutionContexts + Faults report into it (an adapter between contexts and metrics).
 * Have stages as a dtoField of ExecutionContext.
 *
 *
 * We no longer need the add failed, just a finally {reporter.completed()} because it will be failed or not based on
 * having unsuppressed faults. No, there is no reason why having a fault needs to terminate the stage, sometimes they are
 * just dropped (batch imports).
 *
 * ExceptionHandler should create faults in the execution context, not just rethrow. Although rethrowing may be
 * good to reconsider.
 *
 * Also, we need another way to suppress faults, perhaps an all-dtoField-x and all-dtoField-x-by-stage. Or do we make the idea of
 * 'location' more complex instead?
 *
 *
 * Message
 * Warning (Suppressable)
 * Error
 *
 * All have types, locations, and contexts (context<-stage).
 *
 * Sending repeated messages/errors/warnings in with the same dtoField will 'update' the message. If you don't put a message
 * dtoField in, it will generate a new one with a random/sequential id - and return it - update if you want.
 *
 * A context/stage can *fail* as well as contain errors. This is important. A failed stage will fail all nested stages,
 * just like an errors 'location' will fail all sublocations.
 *
 * Perhaps distinguish between normal errors/warnings (for stages) and faults which are errors which have a location.
 *
 * like this:
 *      context.addFault(new Fault(new ModelLocation(field),new Warning(WARN_TYPE,"message")));
 *      context.addError(new Error(ERROR_TYPE,"message));
 *      context.addWarning(new Warning(WARN_TYPE,"message"));
 *      context.addMessage(new Message(PROGRESS,"50% completed"));
 *
 * GOAL:
 *      Have errors on locations like in the codegen
 *      additionally, support line number/files for locations
 *      Have errors in stages and collapsing stages like in gateway
 *      additionally, unify the ErrorFlags with the collapsing contexts.
 *
 * NEW IDEA:
 * 2 separate systems that have touch points. Eg, once having written the faults, you could copy
 * them to the status 'console' as errors and warnings.
 *
 * faultManager.add(new Fault(new ModelLocation(field),new Fault(BAD_TYPE,"Some message")));
 * status.copyFrom(faultManager);
 * status.failIfError()
 *
 * status.fail()
 * status.complete()
 *
 * can be enum, string, UUID, whatever.
 *
 */
public interface SuppressionToken {

    boolean equals(Object o);

}
