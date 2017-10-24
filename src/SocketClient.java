import java.io.*;
import java.net.Socket;

public class SocketClient extends Thread{

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient(Socket client){
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Seance with client " + client.getInetAddress() + " started.");
        try {
            while (true){
                while (!in.ready()){}
                manageGet(in.readLine());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void manageGet(String message){

        System.out.println("Message from " + client.getInetAddress() + ": " + message);

        String[] info = message.split("_");
        String[] args = info[info.length - 1].split("-");



    }

}
