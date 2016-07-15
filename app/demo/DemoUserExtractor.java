package demo;


import com.lightbend.correlation.CorrelationId;
import com.lightbend.quota.japi.User;
import play.mvc.Http;
import play.quota.japi.user.UserExtractor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DemoUserExtractor implements UserExtractor {

    @Override
    public CompletionStage<Optional<User>> userForRequest(Http.RequestHeader rh, CorrelationId correlationId) {
        final String username = rh.getQueryString("username");
        if (username == null) {
            User user = new User("unknown");
            return CompletableFuture.completedFuture(Optional.of(user));
        } else {
            User user = new User("username:" + username);
            return CompletableFuture.completedFuture(Optional.of(user));
        }
    }
}
