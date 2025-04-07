package serverfacade;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebsocketCommunicator extends Endpoint {
    Session session;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
