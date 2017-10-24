import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankConnectionManager {

    public static void main(String[] args) {

        ServerSocket place = null;
        try{
            place = new ServerSocket(1912);
        } catch (IOException e) {e.printStackTrace();}

        while (true){

            try {

                Socket client = place.accept();
                System.out.println("Accepted.");
                new SocketClient(client);
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

}
