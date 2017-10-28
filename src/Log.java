import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;

public class Log {

    private static final String logFileName = "BankLog.txt";

    public static void print(String s, Socket socket){

        String date = Calendar.getInstance().getTime().toString();
        String data = date + "_" + socket.getInetAddress() + "_" + s;
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
