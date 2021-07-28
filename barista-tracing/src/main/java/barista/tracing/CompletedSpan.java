package barista.tracing;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public record CompletedSpan(
        String traceId,
        Optional<String> parentId,
        String spanId,
        String opName,
        Instant start,
        Duration duration) {}
