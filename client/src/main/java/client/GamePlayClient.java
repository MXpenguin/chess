package client;

import serverfacade.ServerMessageObserver;
import websocket.messages.ServerMessage;

public class GamePlayClient implements Client, ServerMessageObserver {
    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public String welcome() {
        return "";
    }

    @Override
    public void notify(ServerMessage message) {

    }
}
