/*
 * This file has been derived from
 * https://github.com/palantir/tracing-java/blob/67c0bce6bbc9cae6aefd1609bacc0f7ac243b5a0\
 * /tracing2/src/main/java/com/palantir/tracing2/Spans.java
 *
 * And is subject to the following license:
 *
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package barista.tracing;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class Spans {
    private static final ThreadLocal<Span> currentSpan = new ThreadLocal<>();
    private static final Map<String, Consumer<CompletedSpan>> consumers = new ConcurrentHashMap<>();

    /** Removes all registered consumers of {@code CompletedSpan}s. */
    public static void clearAllConsumers() {
        consumers.clear();
    }

    /** Registers a consumer of {@code CompletedSpan}s with the provided name. */
    public static void register(String name, Consumer<CompletedSpan> consumer) {
        consumers.put(name, consumer);
    }

    /** Removes the named consumer of {@code CompletedSpan}s. */
    public static void unregister(String name) {
        consumers.remove(name);
    }

    /** Creates a new child span for the thread-attached current span. */
    public static Span forCurrentTrace(String opName) {
        return getThreadSpan().orElse(EmptySpan.INSTANCE).child(opName);
    }

    /** Notifies all registered consumers of this completed span. */
    public static void notify(CompletedSpan span) {
        for (Consumer<CompletedSpan> consumer : consumers.values()) {
            consumer.accept(span);
        }
    }

    static void setThreadSpan(Span span) {
        currentSpan.set(span);
    }

    static Optional<Span> getThreadSpan() {
        return Optional.ofNullable(currentSpan.get());
    }

    private Spans() {}
}
