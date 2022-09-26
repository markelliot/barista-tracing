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
 * A no-overhead, no-op, no-allocation {@link Span} implementation used to make absent traces
 * effectively free. Unlike {@link UnobservedSpan}, this span contains no inherent traceId.
 *
 * <p>See {@link ObservedSpan} for a real {@link Span} implementation.
 */
final class AbsentSpan implements Span {
    public static final Span INSTANCE = new AbsentSpan();

    private AbsentSpan() {}

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
        throw new UnsupportedOperationException("cannot get traceId of an empty span");
    }

    @Override
    public String spanId() {
        throw new UnsupportedOperationException("cannot get spanId of an empty span");
    }
}
