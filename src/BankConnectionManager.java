import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class BankConnectionManager {

    public static void main(String[] args) throws IOException{

        int port = Integer.parseInt(Configurator.config("port"));

        Log.sPrint("SERVER STARTED");
        while (true){
            ServerSocket place = new ServerSocket(port);
            Log.sPrint("Socket configuration: adders = " + InetAddress.getLocalHost() + " port = " + port);
            try {
                new SocketClient(place.accept());
                Log.sPrint("One more client connected.");
            }finally {
                place.close();
            }
        }

    }

}
