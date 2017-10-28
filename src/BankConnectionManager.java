import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class BankConnectionManager {

    public static void main(String[] args) throws IOException{

        System.out.println("START");

        while (true){
            ServerSocket place = new ServerSocket(1912);
            System.out.println(InetAddress.getLocalHost());
            try {
                new SocketClient(place.accept());
                System.out.println("One more client connected.");
            }finally {
                place.close();
            }
        }

    }

}
