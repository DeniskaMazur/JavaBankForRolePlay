import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankConnectionManager {

    public static void main(String[] args) throws IOException{

        while (true){
            System.out.println("start");
            ServerSocket place = new ServerSocket(1912);
            System.out.println("ready");
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
