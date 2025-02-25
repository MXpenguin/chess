package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;
import resultsandrequests.RegisterRequest;
import resultsandrequests.RegisterResult;
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
//        Spark.post("/session", this::login);
        Spark.post("/user", this::register);
    }

    private Object clear(Request req, Response res) {
        try {
            userService.clear();
        } catch(DataAccessException e) {

        }
    }

//    private Object login(Request req, Response res) {
//        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
//        LoginResult result = userService.login(request);
//        return new Gson().toJson(result);
//    }

    private Object register(Request req, Response res) {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        try {
            RegisterResult result = userService.register(request);
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            return "{}";
        }
    }
}
