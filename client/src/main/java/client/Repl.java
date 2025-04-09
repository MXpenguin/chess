package client;

import serverfacade.ServerMessageObserver;
import serverfacade.WebsocketCommunicator;
import ui.DrawChessBoard;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final Client client;

    private DrawChessBoard drawChessBoard;

    public Repl(Client client) {
        this.client = client;
    }

    public void run() {
        System.out.println(client.welcome());
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
