package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import resultsandrequests.*;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;

public class Server {

    private final UserService userService;

    public Server() {
        this.userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
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
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
    }

    private Object clear(Request req, Response res) {
        try {
            userService.clear();
            return "";
        } catch(DataAccessException e) {
            res.status(500);
            return "";
        }
    }

    private Object register(Request req, Response res) {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        try {
            RegisterResult result = userService.register(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: already taken" -> res.status(403);
                    case "Error: bad request" -> res.status(400);
                    default -> res.status(500);
                }
            }
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            return "";
        }
    }

    private Object login(Request req, Response res) {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult result = userService.login(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: unauthorized" -> res.status(401);
                    default -> res.status(500);
                }
            }
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            return "";
        }
    }

    private Object logout(Request req, Response res) {
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            LogoutResult result = userService.logout(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: unauthorized" -> res.status(401);
                    default -> res.status(500);
                }
            }
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            return "";
        }
    }
}
