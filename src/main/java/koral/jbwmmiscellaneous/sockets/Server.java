package koral.jbwmmiscellaneous.sockets;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket listener = new ServerSocket(4212);

            while(true) {
                Socket client = listener.accept();
                System.out.println("" + (new Date()).toString());
                System.out.println("Uzyskano polaczenie z klientem: " + client.getInetAddress());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
