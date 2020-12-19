package koral.jbwmmiscellaneous.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void connectToServer() {
        Socket socket = null;
        try {
            socket = new Socket("51.75.57.188", 4212);
        } catch (IOException e) {

        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
