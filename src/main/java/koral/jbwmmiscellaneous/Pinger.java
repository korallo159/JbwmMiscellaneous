package koral.jbwmmiscellaneous;

import org.apache.commons.net.telnet.TelnetClient;
import org.bukkit.event.EventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Pinger {
    public static void sendPingRequest(String ipAddress){
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Sending Ping Request to " + ipAddress);
        try {
            if (ip.isReachable(5000)) {
                System.out.println("Host is reachable");
                System.out.println(ip.getHostName());
            }
            else
                System.out.println("Sorry! We can't reach to this host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static TelnetClient telnet = new TelnetClient();
    static InputStream inputStream;
    static PrintStream out;

    public static void mainTelnet(String[] args){
        try {
            telnet.connect("tsgorzyce.pl", 12121);
            inputStream = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            int i;
            do{
                i=inputStream.read();
                System.out.println((char) i);
            }
            while (i!=-1);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
