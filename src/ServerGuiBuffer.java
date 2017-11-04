import java.io.IOException;

public interface ServerGuiBuffer {


    String getListOfPeople(String username, String password) throws IOException;
    String pay(String username, String password, String targetUser, int howMuch)throws IOException;
    String checkPassword(String username, String password) throws IOException;
    String newClient(String username, String uid, String password) throws IOException;
    String newClient(String username, String password) throws IOException;
    String changePassword(String username, String password, String newPassword) throws IOException;

}
