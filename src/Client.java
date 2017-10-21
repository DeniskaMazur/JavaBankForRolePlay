import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private static String client;
    private static  String password;
    private static final String commandList = "\n!!!COMMAND LIST!!!!" +
            "\npay(syntax: pay target_user_name how_much)(act: transfer your money(how_much) to target user);\n" +
            "get(syntax: get)(act: show your balance);\n" +
            "new(syntax: new)(act: create new user);\n" +
            "view(syntax: view)(act: show all clients of the bank);\n\n";
    private static final String preMessage = "\nTo create new user print username and password, then use command \"new\"\n";

    public static void main(String[] args) {
        System.out.println(preMessage);
        Console console = System.console();
        Scanner read = new Scanner(System.in);

        while (true){

            System.out.print("Enter your user name: ");
            client = read.nextLine();
            password = String.valueOf(console.readPassword("Enter password for " + client + ": "));

            System.out.println(commandList);
            System.out.print("Enter your command(with arguments): ");
            String command = read.nextLine();

            String[] arguments = command.split( " ");

            switch (arguments[0]){

                case "pay" : {
                    Bank.Pay(client, password, arguments[1] ,Integer.parseInt(arguments[2]));
                    break;
                }

                case "get" : {
                    String s = Bank.GetBalance(client, password);
                    if (s != null) System.out.println(s);
                    break;
                }

                case "new" : {
                    Boolean created = Bank.createNewClient(client, password);
                    if (created){
                        System.out.println("Client created.");
                    }else {
                        System.out.println("Name has been used.");
                    }
                    break;
                }

                case "view" : {
                    Bank.View();
                    break;
                }

            }

            for (int i = 0; i < 10; i++) System.out.println(); //I know that it's a bad idea
            System.out.println("_____________________");

        }

    }

}
