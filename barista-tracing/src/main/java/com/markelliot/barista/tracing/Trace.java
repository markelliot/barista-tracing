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

package com.markelliot.barista.tracing;

/*
 * This file has been derived from
 * https://github.com/palantir/tracing-java/blob/67c0bce6bbc9cae6aefd1609bacc0f7ac243b5a0\
 * /tracing2/src/main/java/com/palantir/tracing2/Trace.java
 */

import java.util.function.Supplier;

public interface Trace {
    /** Adds a new span to this Trace and returns the span. */
    Span rootSpan(String opName);

    /**
     * Adds a new span to this Trace, computing opName only if this is an observed trace, and
     * returns the span.
     */
    Span rootSpan(Supplier<String> opName);

    /**
     * Creates a new Span with a specific parent span id and returns the span. Useful when
     * inheriting a Trace and Span from another process.
     */
    Span withParent(String parentId, String opName);

    /**
     * Creates a new Span with a specific parent span id, computing opName only if this is an
     * observed trace, and returns the span. Useful when inheriting a Trace and Span from another
     * process.
     */
    Span withParent(String parentId, Supplier<String> opName);

    String traceId();
}
