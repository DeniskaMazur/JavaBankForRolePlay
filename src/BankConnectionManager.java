import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankConnectionManager {

    public static void main(String[] args) {

        while (true){

            try {

                ServerSocket place = new ServerSocket(1912);
                Socket client = place.accept();
                System.out.println("Accepted.");
                SocketClient seance = new SocketClient(client);
                seance.run();

            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

}
