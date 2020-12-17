package koral.jbwmmiscellaneous.sockets.socketsnomultithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4212);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String serverResponse = input.readLine();
        while(serverResponse != null){
            System.out.println(serverResponse);
            serverResponse = input.readLine();
        }
        socket.close();
        System.exit(0);

    }
}
