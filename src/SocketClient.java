import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SocketClient extends Thread{

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private Pattern outTrigger = Pattern.compile("out*.");

    public SocketClient(Socket client){
        setDaemon(true);
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        }catch (IOException e){
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Seance with client " + client.getInetAddress() + " started.");
        try {
            while (true){
                while (!in.ready()){}
                String s = in.readLine();
                if (outTrigger.matcher(s).matches()){
                    Out();
                    break;
                }
                manageAct(s);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void manageAct(String message){

        System.out.println("Message from " + client.getInetAddress() + ": " + message);

        if (message.split("_").length <= 5) message += "_null";
        System.out.println(message.split("_").length);

        String[] info = message.split("_");

        System.out.println(Arrays.toString(info));

        switch (info[0]){

            case "0":{

                switch (info[1]){

                    case "in":{

                        manageLogIn(info[2], info[3], info[4]);
                        break;

                    }

                    case "up":{

                        manageSingUp(info[2], info[3], info[4]);
                        break;

                    }

                }

                break;
            }

            case "1":{

                manageAct(info[1], info[2], info[3], info[4], info[5]);
                break;

            }

        }

    }

    private void manageLogIn(String card_username, String name, String password){

        StringBuilder builder = new StringBuilder();

        if (Bank.isClient(name, card_username)) {
            System.out.println("name");
            if (Bank.checkPasswordNotConf(name, password, true, card_username)){
                System.out.println("pass");
                builder.append("true_");
                builder.append(Bank.GetBalance(name, password, true, card_username));
            } else builder.append("false_0");
        } else builder.append("false_0");

        Throw(builder.toString());

    }

    private void manageSingUp(String name, String uid, String password){

        StringBuilder builder = new StringBuilder();

        builder.append(Bank.createNewClient(name, uid, password, true));
        builder.append("_0");

        Throw(builder.toString());

    }

    private void manageAct(String card_username, String name, String password, String act, String args){

        String[] arguments = args.split("!");
        StringBuilder builder = new StringBuilder();

        switch (act){

            case "pay":{

                builder.append(Bank.Pay(name, password, arguments[0], Integer.parseInt(arguments[1]), true, card_username));
                builder.append("_");
                builder.append(Bank.GetBalance(name, password, true, card_username));
                break;

            }

            case "change":{

                System.out.println(arguments[0]);
                builder.append(Bank.changePassword(name, password, arguments[0], true, card_username));
                break;

            }

            case "view":{

                builder.append(Bank.viewAsStr());
                break;

            }

        }

        Throw(builder.toString());

    }

    private void Throw(String msg){

        System.out.println(msg);
        out.println(msg);

    }

    private void Out(){

        try {
            client.close();
        }catch (IOException e){e.printStackTrace();}

    }

}
