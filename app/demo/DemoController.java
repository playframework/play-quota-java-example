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
