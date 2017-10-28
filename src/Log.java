import java.net.Socket;
import java.util.Calendar;

public class Log {

    public static void print(String s, Socket socket){

        String date = Calendar.getInstance().getTime().toString();
        System.out.println(date + "_" + socket.getInetAddress() + "_" + s);

    }

}
