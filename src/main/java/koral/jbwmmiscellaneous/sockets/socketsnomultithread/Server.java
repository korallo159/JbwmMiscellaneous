package koral.jbwmmiscellaneous.sockets.socketsnomultithread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException{
            ServerSocket serverSocket = new ServerSocket(4212);
        System.out.println("[SERVER] waiting for client connection");
            Socket client = serverSocket.accept();
        System.out.println("[SERVER] connected");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println((new Date()).toString());
        out.println("Test polaczenia");
        System.out.println("[SERVER] wyslano date polaczenia... zamykanie");
        client.close();
        serverSocket.close();
    }
}
