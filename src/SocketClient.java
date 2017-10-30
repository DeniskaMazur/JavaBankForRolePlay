import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SocketClient extends Thread{

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private Pattern outTrigger = Pattern.compile("out*.");
    private static final String card = "c";

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
        Log.print("STARTED", client);
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
        Log.print("message: " + message, client);

        if (message.split("_").length <= 5) message += "_null";

        String[] info = message.split("_");
        Log.print("split: " + Arrays.toString(info), client);

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

        Boolean format = true;
        if (card_username.equals(card)) format = checkFormat(password);
        if (Bank.isClient(name, card_username) && format) {
            Log.print("name coincided: " + name, client);
            if (Bank.checkPasswordNotConf(name, password, true, card_username)){
                Log.print("password coincided: " + password, client);
                builder.append("true_");
                builder.append(Bank.GetBalance(name, password, true, card_username));
            } else builder.append("false_0");
        } else builder.append("false_0");

        Throw(builder.toString());

    }

    private void manageSingUp(String name, String uid, String password){

        StringBuilder builder = new StringBuilder();

        if (checkFormat(uid)){
            builder.append(Bank.createNewClient(name, uid, password, true));
            builder.append("_0");
        } else builder.append("false_0");

        Throw(builder.toString());

    }

    private void manageAct(String card_username, String name, String password, String act, String args){

        String[] arguments = args.split("!");
        StringBuilder builder = new StringBuilder();

        Boolean format = true;
        if (card_username.equals(card_username)) format = checkFormat(password);

        switch (act){

            case "pay":{

                if (format){
                    builder.append(Bank.Pay(name, password, arguments[0], Integer.parseInt(arguments[1]), true, card_username));
                    builder.append("_");
                    builder.append(Bank.GetBalance(name, password, true, card_username));
                    break;
                }

            }

            case "change":{

                if (format){
                    builder.append(Bank.changePassword(name, password, arguments[0], true, card_username));
                    break;
                }

            }

            case "view":{

                builder.append(Bank.viewAsStr());
                break;

            }

        }

        Throw(builder.toString());

    }

    private void Throw(String msg){

        Log.print("sended: " + msg, client);
        out.println(msg);

    }

    private void Out(){

        try {
            Log.print("closing", client);
            client.close();
            Log.print("closed", client);
        }catch (IOException e){e.printStackTrace();}

    }

    private Boolean checkFormat(String integer){
        Boolean format = true;
        try {
            Integer.parseInt(integer);
        }catch (NumberFormatException e){format = false;}
        return format;
    }

}
