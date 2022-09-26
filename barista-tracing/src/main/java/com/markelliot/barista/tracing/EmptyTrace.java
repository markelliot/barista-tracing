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

import java.util.function.Supplier;

public final class EmptyTrace implements Trace {

    private final String traceId;
    private final EmptySpan singletonSpan;

    EmptyTrace(String traceId) {
        this.traceId = traceId;
        this.singletonSpan = new EmptySpan(traceId);
    }

    @Override
    public Span rootSpan(String opName) {
        return getSpan();
    }

    @Override
    public Span rootSpan(Supplier<String> opName) {
        return getSpan();
    }

    @Override
    public Span withParent(String parentId, String opName) {
        return getSpan();
    }

    @Override
    public Span withParent(String parentId, Supplier<String> opName) {
        return getSpan();
    }

    @Override
    public String traceId() {
        return traceId;
    }

    /**
     * Attach thread state and return the singleton span.
     *
     * <p>Note that we perform this lazily to maintain the contract that creating a trace does not
     * immediately attach trace state to the current thread.
     */
    private Span getSpan() {
        Spans.setThreadSpan(singletonSpan);
        return singletonSpan;
    }
}
