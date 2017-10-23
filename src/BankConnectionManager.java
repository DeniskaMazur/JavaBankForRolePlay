import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BankConnectionManager {

    public static void main(String[] args) {

        while (true){

            try {

                ServerSocket place = new ServerSocket(1912);
                Socket client = place.accept();
                System.out.println("Accepted: " + place.getInetAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                while (true){
                    while (!reader.ready()){}
                    System.out.println(reader.readLine());
                }

            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

}
