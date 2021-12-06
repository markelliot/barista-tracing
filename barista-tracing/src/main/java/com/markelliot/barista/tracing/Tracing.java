/*
 * (c) Copyright 2021 Mark Elliot. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * This file has been derived from
 * https://github.com/palantir/tracing-java/blob/67c0bce6bbc9cae6aefd1609bacc0f7ac243b5a0\
 * /tracing2/src/main/java/com/palantir/tracing2/Tracing.java
 */

package com.markelliot.barista.tracing;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public final class Tracing {
    private Tracing() {}

    public static Runnable wrap(String opName, Runnable runnable) {
        Optional<Span> current = Spans.getThreadSpan();
        return current.map(
                        span ->
                                (Runnable)
                                        () -> {
                                            try (Span ignored = span.child(opName)) {
                                                runnable.run();
                                            }
                                        })
                .orElse(runnable);
    }

    public static <T> Callable<T> wrap(String opName, Callable<T> callable) {
        Optional<Span> current = Spans.getThreadSpan();
        return current.map(
                        span ->
                                (Callable<T>)
                                        () -> {
                                            try (Span ignored = span.child(opName)) {
                                                return callable.call();
                                            }
                                        })
                .orElse(callable);
    }

    public static ExecutorService copyTraceStateOnSubmitExecutorService(
            String opName, ExecutorService delegate) {
        return new WrappingExecutorService(delegate) {
            @Override
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return wrap(opName, callable);
            }
        };
    }

    public static ScheduledExecutorService copyTraceStateOnSubmitScheduledExecutorService(
            String opName, ScheduledExecutorService delegate) {
        return new WrappingScheduledExecutorService(delegate) {
            @Override
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return wrap(opName, callable);
            }
        };
    }
}
