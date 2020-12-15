package koral.jbwmmiscellaneous;
import java.io.IOException;
import java.net.Socket;

public class SocketClient  {

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localhost", 4999);
    }
}
