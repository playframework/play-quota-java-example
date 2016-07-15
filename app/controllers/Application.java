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
