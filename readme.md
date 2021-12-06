# barista-tracing
[![Publish develop](https://github.com/markelliot/barista-tracing/actions/workflows/publish.yml/badge.svg)](https://github.com/markelliot/barista-tracing/actions/workflows/publish.yml)

An ergonomics-focused library for managing Zipkin-style tracing, both at application boundaries
and for spans within an application. This library offers both explicit and implicit Span-parenting,
and efficiently handles discarding tracing when the observability of a trace is disabled.

New traces are commonly created through a web framework (like Barista), or explicitly:

```java
Trace trace = Traces.newTrace(Id.randomId(), true);
```

Most of the useful information in tracing comes from the `Span`, a time-bounded, named unit of work
with optional parent to indicate that a particular Span is a smaller unit of work taking place
within a larger one.

The methods on `Span` enable creation of child and sibling Spans that are part of the same
overall Trace. Sibling spans are a short-cut method for ending the current Span and
starting another Span with the same parent. On completion/close, if the `Trace` is set to be
observable, this framework will emit a `CompletedSpan` to all subscribers.

Subscribers register to listen to `CompletedSpan`s using `Spans#register`.

Users may choose to explicitly pass around `Span` instances to manually connect children
and siblings to traces, and to complete traces. A common usage pattern is to place the Span
within a try-with-resources block:
```java
Trace trace = Traces.newTrace(Id.randomId(), true);
try (Span rootSpan = trace.rootSpan("operationName")) {
    // traced work for root
    try (Span childSpan = rootSpan.child("childOperation")) {
        // traced work for child
    }
    // work also in the root span
}
// untraced work
```

Alternatively, users may choose to explicitly pass around `Span` instances to manually connect 
children and siblings to traces, and to complete traces. Again, a common usage pattern is to place 
the Span within a try-with-resources block:
```java
Trace trace = Traces.newTrace(Id.randomId(), true);
try (Span rootSpan = trace.rootSpan("operationName")) {
    // traced work for root
    try (Span childSpan = rootSpan.child("childOperation")) {
       // traced work for child
    }
    // work also in the root span
}
// untraced work
```

Users may also take advantage of implicit trace propagation using `Spans#forCurrentTrace(String)`. 
This class always updates local thread state to match the most recently created span or the parent 
of the most recently completed span. It's not possible to obtain the "current" implicit span, only 
to derive a child span, and so generally speaking it's not possible to close implicitly passed 
parent spans.

Users should use implicit spans similar to explicit spans:
```java
try (Span ignored = Spans.forCurrentTrace("operationName")) {
   // traced work
}
```

Implicit spans are especially valuable when tracing may traverse many levels of existing code,
and likely should be preferred in scenarios where passing the explicit Span around would create
heavy code churn or add noise to code structure.

Because barista-tracing's Span always interacts with thread state, one may intermix implicit and 
explicit spans as desired. For instance:

```java
Trace trace = Traces.newTrace(Id.randomId(), true);
try (Span rootSpan = trace.rootSpan("operationName")) {
    executor.submit(() -> {
        try (Span childSpan = rootSpan.child("childOperation")) {
            cpuIntensiveTask();
        }
    });
}

// for some implementation of
void cpuIntensiveTask() {
    try (Span step1 = Spans.forCurrentTrace("cpuIntensiveTask.step1")) {
        // do step 1
    }
    try (Span step2 = Spans.forCurrentTrace("cpuIntensiveTask.step2")) {
        // do step 2
    }
}
```

Users should generally use `Tracing`'s utility methods to transit thread boundaries. To trace
a `Callable` or `Runnable` submitted to another thread but track the _current_ trace:

```java
// track cpuIntensiveTask as a child of the current Span
Runnable work = Tracing.wrap(() -> cpuIntensiveTask()); 
```

Or, to make an `ExecutorService` or `ScheduledExecutorService` that always wraps work submitted to
happen as a child of the current span, use `Tracing#copyTraceStateOnSubmitExecutorService` or
`Tracing#copyTraceStateOnSubmitScheduledExecutorService`, respectively.

# License
This project is made available under the [Apache 2.0 License](/LICENSE).
