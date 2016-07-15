package demo;

import com.lightbend.correlation.CorrelationId;
import com.lightbend.quota.japi.*;
import com.lightbend.quota.japi.judge.Access;
import com.lightbend.quota.japi.judge.AccessDenied;
import com.lightbend.quota.japi.judge.AccessGranted;
import play.mvc.Result;
import play.mvc.Results;
import play.quota.japi.format.ResultFormatter;

import java.time.Duration;
import java.time.Instant;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DemoResultFormatter implements ResultFormatter {

    @Override
    public CompletionStage<Result> formatGranted(Instant now, User user, int requestCost, OptionalInt balance, Quota quota, Result result, CorrelationId correlationId) {
        return display(AccessGranted.getInstance(), now, user, requestCost, balance, quota, correlationId);
    }

    @Override
    public CompletionStage<Result> formatDenied(Instant now, User user, int requestCost, OptionalInt balance, Quota quota, CorrelationId correlationId) {
        return display(AccessDenied.getInstance(), now, user, requestCost, balance, quota, correlationId);
    }

    private CompletionStage<Result> display(Access access, Instant now, User user, int requestCost, OptionalInt balance, Quota quota, CorrelationId correlationId) {

        QuotaDisplay quotaDisplay;
        if (quota instanceof Zero) {
            quotaDisplay = ZeroDisplay.getInstance();
        } else if (quota instanceof Unlimited) {
            quotaDisplay = UnlimitedDisplay.getInstance();
        } else if (quota instanceof RateLimited) {
            RateLimited rl = (RateLimited) quota;
            long tickSeconds = rl.getTickSize() / 1000;
            Instant nextRefill = rl.nextRefillTime(now);
            final long secondsUntilRefill = Duration.between(now, nextRefill).getSeconds();
            quotaDisplay = new RateLimitedDisplay(secondsUntilRefill, tickSeconds, balance.getAsInt(), rl.getMaxBalance());
        } else {
            throw new IllegalArgumentException("Unknown type quota " + quota);
        }

        return CompletableFuture.completedFuture(Results.ok(views.html.demo.render(access, user, requestCost, quotaDisplay, correlationId)));

    }

}
