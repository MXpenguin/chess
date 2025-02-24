package server;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;
import service.UserService;
import spark.*;

public class Server {

    UserService userService;

    public Server() {
        this.userService = new UserService(new MemoryUserDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes() {
        Spark.post("/session", this::login);
    }

    private Object login(Request req, Response res) {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        return new Gson().toJson(result);
    }
}
