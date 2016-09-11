package io.sponges.bot.modules.rest.route.generic;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.modules.rest.RequestWrapper;
import org.json.JSONObject;
import spark.Response;

public abstract class GenericUserRoute extends GenericNetworkRoute {

    public GenericUserRoute(Method method, String route) {
        super(method, "/users/:user" + route);
    }

    protected abstract JSONObject execute(RequestWrapper request, Response response, JSONObject json, User user);

    @Override
    protected JSONObject execute(RequestWrapper request, Response response, JSONObject json, Network network) {
        UserManager userManager = network.getUserManager();
        String userId = request.getRequest().params("user");
        if (userId == null) {
            setError("Invalid user");
            return json;
        }
        if (!userManager.isUser(userId)) {
            User user = userManager.loadUserSync(userId);
            if (user == null) {
                setError("User not found in client");
            } else {
                execute(request, response, json, user);
            }
        } else {
            User user = userManager.getUser(userId);
            execute(request, response, json, user);
        }
        return json;
    }
}
