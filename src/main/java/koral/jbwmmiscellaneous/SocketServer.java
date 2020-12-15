package koral.jbwmmiscellaneous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer  {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(9);
    public static void main(String[] args){
        try {
            ServerSocket listener = new ServerSocket(4999);
            while(true) {
                System.out.println("[SERVER] waiting for client connection...");
                Socket client = listener.accept();
                System.out.println("[SERVER] Connected to client" + client.getInetAddress());
                ClientHandler clientThread = new ClientHandler(client, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
