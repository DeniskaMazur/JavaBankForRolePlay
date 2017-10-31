import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Log {

    private static final String logFileName = "BankLog.txt";
    private static final Pattern postFix = Pattern.compile(".*.___pr");

    public static void print(String s, Socket socket){

        String data = socket.getInetAddress() + "_" + s + "___pr";
        sPrint(data);

    }

    public static void sPrint(String s){

        String date = Calendar.getInstance().getTime().toString();
        if (!postFix.matcher(s).matches()) s += "___ss";
        String data = date + "_" + s;
        try {
            FileWriter logWriter = new FileWriter(new File(logFileName), true);
            System.out.println(data);
            logWriter.write(data + "\n");
            logWriter.flush();
        }catch (IOException e){
            System.out.println("WRITING LOG ERROR!!!");
            e.printStackTrace();
        }

    }

}
