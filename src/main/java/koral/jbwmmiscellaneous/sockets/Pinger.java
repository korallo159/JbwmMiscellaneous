package koral.jbwmmiscellaneous.sockets;


import java.io.IOException;
import java.net.InetAddress;
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

}
