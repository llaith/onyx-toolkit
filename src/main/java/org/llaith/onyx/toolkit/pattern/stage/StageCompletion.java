/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.stage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 *
 */
public class StageCompletion {

    private final UUID uuid;

    private final String breadcrumb;

    private final LocalDateTime timeStarted;

    private final LocalDateTime timeCompleted;

    private final boolean success;

    public StageCompletion(final Context context, final boolean success) {

        this.uuid = context.uuid();

        this.breadcrumb = context.breadcrumb();

        this.timeStarted = context.timeStarted();

        this.timeCompleted = LocalDateTime.now();

        this.success = success;

    }

    public UUID uuid() {
        return uuid;
    }

    public String breadcrumb() {
        return this.breadcrumb;
    }

    public LocalDateTime timeStarted() {
        return this.timeStarted;
    }

    public long timeCompleted(ChronoUnit chronoUnit) {

        return chronoUnit.between(this.timeStarted, this.timeCompleted);

    }

    public boolean isSuccess() {
        return success;
    }

}
