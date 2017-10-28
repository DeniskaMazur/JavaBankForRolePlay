import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class BankConnectionManager {

    public static void main(String[] args) throws IOException{

        while (true){
            System.out.println("start");
            ServerSocket place = new ServerSocket(1912);
            System.out.println("ready");
            System.out.println(InetAddress.getLocalHost());
            try {
                new SocketClient(place.accept());
                System.out.println("acepted");
            }finally {
                place.close();
                System.out.println("closed");
            }
        }

    }

}
