/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.display.memo;

import org.llaith.onyx.toolkit.pattern.display.memo.impl.TextMemoRenderer;
import org.llaith.onyx.toolkit.exception.ExceptionUtil;
import org.llaith.onyx.toolkit.lang.FlowableText;

/**
 *
 */
public class MemoFactory {

    public static Memo memo(final String message) {

        return new Memo(
                new Section(0).withBlock(
                        new ParagraphBlock().withText(message)));
    }

    public static FlowableText textMemo(final String message) {

        final Memo memo = memo(message);

        return new TextMemoRenderer(0, 4)
                .render(memo);

    }

    public static FlowableText textMemoOf(final Throwable exception) {

        return textMemo(ExceptionUtil.stackTraceToString(exception));

    }

    public static FlowableText textMemoOf(final StackTraceElement[] trace) {

        return textMemo(ExceptionUtil.stackTraceElementsToString(trace));

    }

}
