import java.io.*;
import java.net.*;

public class ClientLogic implements ServerGuiBuffer {

    private PrintWriter out;
    private BufferedReader in;


    ClientLogic(String ip, int port){
        try {
            Socket server;
            InetAddress addr = InetAddress.getByName(ip);
            server = new Socket(addr, port);
            out = new PrintWriter(new DataOutputStream(server.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }
        catch (Exception e) { e.printStackTrace(); }

    }

    @Override
    public String getListOfPeople(String username, String password) throws IOException{
        out.println("1_u_" + username + "_" + password + "_view");
        return in.readLine();
    }

    @Override
    public String pay(String username, String password, String targetUser, int howMuch) throws IOException{
        out.println("1_u_" + username + "_" + password+ "_pay_" + targetUser + "!" + howMuch);
        return in.readLine();
    }

    @Override
    public String checkPassword(String username, String password) throws IOException {
        String out_data = "0_in_u_" + username+"_" + password;
        out.println(out_data);
        return in.readLine();
    }

    @Override
    public String newClient(String username, String password) throws IOException {
        return null;
    }

    @Override
    public String newClient(String username, String uid, String password) throws IOException {
        out.println("0_up_"+username+ "_"+uid+"_"+password);
        return in.readLine();
    }

    @Override
    public String changePassword(String username, String password, String newPassword) throws IOException {
        out.println("1_u_" + username + "_" + password + "_change_" + newPassword);
        return in.readLine();
    }

}
