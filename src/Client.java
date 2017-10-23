import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private static String client;
    private static  String password;
    private static final String commandList = "\n!!!COMMAND LIST!!!!" +
            "\npay(syntax: pay target_user_name how_much)(act: transfer your money(how_much) to target user);\n" +
            "change(syntax: change old_password new_password)\n" +
            "out(syntax: out)(act: log out)";
    private static final String preMessage = "\nTo create new user just print username and password(client will be created automatically)\n";

    public static void main(String[] args) {

        Console console = System.console();
        Scanner read = new Scanner(System.in);

        while (true){

            System.out.println(preMessage);
            System.out.print("Enter your user name: ");
            client = read.nextLine();
            password = String.valueOf(console.readPassword("Enter password for " + client + ": "));
            createIfNotCreated(client, password);
            Boolean stop = Bank.checkPassword(client, password);

            while (stop){

                printBalance(client, password);

                System.out.println(commandList);
                System.out.print("Enter your command(with arguments): ");
                String command = read.nextLine();

                String[] arguments = command.split( " ");

                switch (arguments[0]){

                    case "pay" : {
                        System.out.println("Here are bank clients:");
                        Bank.View();
                        System.out.print("Enter target name: ");
                        String target = read.nextLine();
                        System.out.print("Enter count: ");
                        int count = read.nextInt();
                        read.nextLine(); //Bugging piece of shit
                        if (Bank.isClient(target)) Bank.Pay(client, password, target , count);
                        else System.out.println("There is no such target");
                        break;
                    }

                    case "change" : {
                        if (arguments[1].equals(password)){
                            if (Bank.changePassword(client, password, arguments[2])){
                                password = arguments[2];
                                System.out.println("Password changed.");
                            } else {
                                System.out.println("Password change error.");
                            }
                        } else {
                            System.out.println("Wrong old password.");
                        }
                        break;
                    }

                    case "out" : {
                        stop = false;
                        System.out.println("Logging our...");
                        break;
                    }

                }
            }

            for (int i = 0; i < 10; i++) System.out.println(); //I know that it's a bad idea
            System.out.println("_____________________");

        }

    }

    private static void createIfNotCreated(String userName, String password){

        if (!Bank.isClient(userName)){

            Boolean created = Bank.createNewClient(userName, password);
            if (created){
                System.out.println("Client created.");
            }else {
                System.out.println("Name has been used.");
            }

        }

    }

    private static void printBalance(String userName, String password){
        String s = Bank.GetBalance(userName, password);
        if (s != null) System.out.println("__YOUR BALANCE IS: " + Bank.GetBalance(userName, password));
    }

}
