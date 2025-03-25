package server;

import com.google.gson.Gson;
import resultsandrequests.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        String path = "/user";
        String method = "POST";
        String body = toJson(request);
        return makeRequest(path, method, null, body, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        String path = "/session";
        String method = "POST";
        String body = toJson(request);
        return makeRequest(path, method, null, body, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        String path = "/session";
        String method = "DELETE";
        String authToken = request.authToken();
        return makeRequest(path, method, authToken, "{}", LogoutResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws ResponseException {
        String path = "/game";
        String method = "GET";
        String authToken = request.authToken();
        return makeRequest(path, method, authToken, null, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        String path = "/game";
        String method = "POST";
        String authToken = request.getAuthToken();
        String body = toJson(request);
        return makeRequest(path, method, authToken, body, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws ResponseException {
        String path = "/game";
        String method = "PUT";
        String authToken = request.getAuthToken();
        String body = toJson(request);
        return makeRequest(path, method, authToken, body, JoinGameResult.class);
    }

    private <T> T makeRequest(String path, String method, String authToken, String body, Class<T> resultClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (body != null) {
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(body.getBytes());
                }
            }

            http.connect();

            throwIfNotSuccessful(http);

            return readBody(http, resultClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(status, respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private String toJson(Object object) {
        return new Gson().toJson(object);
    }

    private boolean isSuccessful(int status) {
        return 200 <= status && status < 300;
    }
}
