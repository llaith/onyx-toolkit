/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.stage;

import org.llaith.onyx.toolkit.pattern.display.memo.MemoFactory;
import org.llaith.onyx.toolkit.pattern.display.status.StatusReporter;
import org.llaith.onyx.toolkit.exception.ExceptionSuppressor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class CompoundStageListener implements StageListener {

    private final List<StageListener> listeners = new ArrayList<>();

    public CompoundStageListener() {

        super();

    }

    public CompoundStageListener(final List<StageListener> listeners) {

        this.listeners.addAll(listeners);

    }

    public void addAllListeners(final Collection<StageListener> listeners) {
        this.listeners.addAll(listeners);
    }

    public void addListener(final StageListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final StageListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void onStart(final Context context) {

        final ExceptionSuppressor suppressor = new ExceptionSuppressor();

        for (final StageListener it : this.listeners) {

            try {
                it.onStart(context);
            } catch (Exception e) {
                suppressor.suppress(e);
            }

        }

        if (suppressor.hasSuppressed()) this.reportException(
                context,
                suppressor.exception());

    }

    @Override
    public void onSuccess(final Context context) {

        final ExceptionSuppressor suppressor = new ExceptionSuppressor();

        for (final StageListener it : this.listeners) {
            try {
                it.onSuccess(context);
            } catch (Exception e) {
                suppressor.suppress(e);
            }
        }

        if (suppressor.hasSuppressed()) this.reportException(
                context,
                suppressor.exception());

    }

    @Override
    public void onFailure(final Context context) {

        final ExceptionSuppressor suppressor = new ExceptionSuppressor();

        for (final StageListener it : this.listeners) {
            try {
                it.onFailure(context);
            } catch (Exception e) {
                suppressor.suppress(e);
            }
        }

        if (suppressor.hasSuppressed()) this.reportException(
                context,
                suppressor.exception());

    }

    private void reportException(final Context context, final Exception e) {
        context.reportAt(StatusReporter.Threshold.ERROR)
               .emptyLine()
               .newLine("Ignoring exception in stage-listener:")
               .newLine(MemoFactory.textMemoOf(e).asString());

    }

}
