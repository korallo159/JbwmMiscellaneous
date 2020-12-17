package koral.jbwmmiscellaneous.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 4212);

        socket.close();

    }
}
