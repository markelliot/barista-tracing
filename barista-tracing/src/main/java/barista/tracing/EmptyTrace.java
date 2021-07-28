package barista.tracing;

import java.util.function.Supplier;

public final class EmptyTrace implements Trace {

    private final String traceId;

    EmptyTrace(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public Span rootSpan(String opName) {
        return EmptySpan.INSTANCE;
    }

    @Override
    public Span rootSpan(Supplier<String> opName) {
        return EmptySpan.INSTANCE;
    }

    @Override
    public Span withParent(String parentId, String opName) {
        return EmptySpan.INSTANCE;
    }

    @Override
    public Span withParent(String parentId, Supplier<String> opName) {
        return EmptySpan.INSTANCE;
    }

    @Override
    public String traceId() {
        return traceId;
    }
}
