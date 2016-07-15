# Play User Quotas with Java

This is an example application that shows the use of Play User Quotas for a single Play instance.

## Installation

To add Play User Quotas to a Play application, please add the following to `build.sbt`:

```
libraryDependencies ++= Seq(
  "com.lightbend.quota" %% "play-quota" % "<version>",
  "com.lightbend.quota" %% "play-quota-java" % "<version>"
)
```

## Default Quota Action to Controller

The quota action for a Play application is configured through Guice using an injected QuotaAction, using the `@CheckQuota` annotation:

``` java
package controllers;

import play.mvc.*;
import play.quota.japi.CheckQuota;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Application extends Controller {

    /**
     * Adds the check quota annotation based off the default quota action.
     */
    @CheckQuota
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
```

## Default User Quotas Configuration

The default Play User Quotas configuration is set with an in-memory judge, which has a fixed user quota defined.  The example configuration is as follows:

```
play.quota.action.default {
  requestCost = 1

  judge {
    # Use a local in-memory judge
    type = memory
    # We set a rate of 50 requests every 5 minutes. This should
    # block robots but not normal users.
    userQuotas {
      type = fixed
      quota {
        maxBalance = 50
        refillAmount = 50
        tickSize = 5 minutes
      }
    }
  }

  # Use IP addresses as users
  userExtractor {
    type = ipAddress
  }

  # Format like Twitter
  resultFormatter {
    type = rest
  }

  # Use flake ids
  correlationIdExtractor {
    type = flakeId
  }
}
```

## Named Play User Quota Action

You can define a custom quota action as follows:

```
GET     /demo                       demo.DemoController.index
```

The DemoController is named directly through Guice with the `@CheckQuota("demo")` annotation.

``` java
package demo;

import play.mvc.Controller;
import play.mvc.Result;
import play.quota.japi.CheckQuota;

public class DemoController extends Controller {

    @CheckQuota("demo")
    public Result index() {
        return ok("Demo Action Result");
    }
}

```

The `demo` action is defined with custom names and types as follows:

```
play.quota.action.demo {
  requestCost = 1
  judge {
    type = named
    name = demo
  }
  userExtractor.type = demo.DemoUserExtractor
  correlationIdExtractor.type = demo.DemoCorrelationIdExtractor
  resultFormatter.type = demo.DemoResultFormatter
}

quota.judge.demo {
  type = memory
  userQuotas {
    type = rule
    defaultQuota.type = zero
    rule {
      matcher {
        type = regex
        regex = "^username:(.*)$"
      }
      quota {
        maxBalance = 5
        refillAmount = 5
        tickSize = 1 minute
      }
    }
  }
}
```

Because the types are defined explicitly for the userExtractor, correlationIdExtractor, and the resultFormatter, the `demo.DemoUserExtractor`, `demo.DemoCorrelationIdExtractor` and `demo.DemoResultFormatter` classes are used.

## Testing

When you run your application, this action will have service quotas enforced.

Using httpie (http://httpie.org) you can see the effect below:

```
$ http --headers GET http://localhost:9000
HTTP/1.1 200 OK
...
X-Correlation-Id: 9ABIYFLjqjayoZWdtY
X-Rate-Limit-Limit: 50
X-Rate-Limit-Remaining: 49
X-Rate-Limit-Reset: 1442719800
```

When the limit is exceeded, instead of a 200 OK response, a 429 response is returned:

```
$ http --headers GET http://localhost:9000
HTTP/1.1 429 Too Many Requests
...
X-Correlation-Id: 9ABIYV4bODOXa3dBPE
X-Rate-Limit-Limit: 50
X-Rate-Limit-Remaining: 0
X-Rate-Limit-Reset: 1442722800
```

These quotas are enforced for each IP address. Each IP address can make 50 requests every 5 minutes. Rate limiting information is presented to the user using typical REST headers.
