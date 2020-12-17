package koral.jbwmmiscellaneous.sockets.socketsmultithread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class SocketClient  {

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localhost", 4999);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Data polaczenia:" + (new Date()).toString());
    }
}
