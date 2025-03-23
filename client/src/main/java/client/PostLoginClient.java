package client;

public class PostLoginClient implements Client {
    @Override
    public String eval(String eval) {
        return "";
    }

    @Override
    public String help() {
        return """
                create <NAME>
                list
                join <ID> [WHITE|BLACK]
                observe <ID>
                logout
                quit
                help
                """;
    }
}
