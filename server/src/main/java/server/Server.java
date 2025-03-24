package server;

import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.*;
import resultsandrequests.*;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

public class Server {

    private final UserService userService;
    private final GameService gameService;

    public Server() {
        // Edit these to switch between a memory and persistent storage
        AuthDAO authDAO = null;
        UserDAO userDAO = null;
        GameDAO gameDAO = null;
        try {
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(authDAO, gameDAO);
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
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
    }

    private Object clear(Request req, Response res) {
        try {
            userService.clear();
            gameService.clear();
            return "";
        } catch(DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
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
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
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
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
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
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object createGame(Request req, Response res) {
        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            CreateGameResult result = gameService.createGame(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: unauthorized" -> res.status(401);
                    default -> res.status(500);
                }
            }
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object listGames(Request req, Response res) {
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        try {
            ListGamesResult result = gameService.listGames(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: unauthorized" -> res.status(401);
                    default -> res.status(500);
                }
            }
            System.out.println(new Gson().toJson(result));//TODO
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res) {
        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            JoinGameResult result = gameService.joinGame(request);
            if (result.getMessage() != null) {
                String message = result.getMessage();
                switch(message) {
                    case "Error: bad request" -> res.status(400);
                    case "Error: unauthorized" -> res.status(401);
                    case "Error: already taken" -> res.status(403);
                    default -> res.status(500);
                }
            }
            return new Gson().toJson(result);
        } catch(DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
