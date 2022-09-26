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
 * /tracing2/src/main/java/com/palantir/tracing2/EmptySpan.java
 */

/**
 * A no-overhead, no-op, no-allocation {@link Span} implementation used to make unobserved traces
 * effectively free. This span keeps track of the original traceId and makes the traceId available
 * to callers.
 *
 * <p>See {@link ObservedSpan} for a real {@link Span} implementation.
 */
final class UnobservedSpan implements Span {
    private final String traceId;

    UnobservedSpan(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public Span sibling(String _opName) {
        return this;
    }

    @Override
    public Span child(String _opName) {
        return this;
    }

    @Override
    public void close() {}

    @Override
    public String traceId() {
        return traceId;
    }

    @Override
    public String spanId() {
        throw new UnsupportedOperationException("cannot get spanId of an empty span");
    }
}
