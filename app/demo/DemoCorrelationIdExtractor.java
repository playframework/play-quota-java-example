package demo;

import com.lightbend.correlation.*;
import play.mvc.Http;
import play.quota.japi.correlation.CorrelationIdExtractor;

/**
 * Returns an ID that can be used to trace the request.
 */
public class DemoCorrelationIdExtractor implements CorrelationIdExtractor {
    @Override
    public CorrelationId correlationIdForRequest(Http.RequestHeader rh) {
        String id = "[" + System.nanoTime() + " " + rh.method() + " " + rh.path() + "]";
        return new StringCorrelationId(id);
    }
}
