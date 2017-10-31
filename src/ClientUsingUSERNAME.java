import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientUsingUSERNAME {
    private static Socket server;
    static String ANSI_CLEAR_SEQ = "\u001b[2J";

    public static void main(String[] args) {
            Scanner read = new Scanner(System.in);
            System.out.print("Введите IP адрес:");
            String IP = read.nextLine();
            System.out.print("Введите порт: ");
            int port = read.nextInt();
            System.out.println(ANSI_CLEAR_SEQ);
            while (true) {
                try {
                    InetAddress addr = InetAddress.getByName(IP);
                    server = new Socket(addr, port);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("Напишите, пожалуйста, ваш Логин");
                read.nextLine();
                String username = read.nextLine();
                action(username);
            }
        }
    private static void action(String username) {
        try {
            Scanner read = new Scanner(System.in);
            PrintWriter out = new PrintWriter(new DataOutputStream(server.getOutputStream()), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            // Описать все по протоколу.
            System.out.print("Введите ваш пинкод:");
            String pincode = Integer.toString(read.nextInt());
            String out_data = "0_" + "in_" + "u_" + username+"_" + pincode.hashCode();
            System.out.println("out_data: " + out_data);
            out.println(out_data.replaceAll("\n", ""));
            String isLoggedIn = in.readLine();
            System.out.println("Ответ сервера: " + isLoggedIn);
            int userAnswer = 3;
            if ("true".equals(isLoggedIn.split("_")[0])) {
                while(userAnswer != 2) {
                    System.out.println(ANSI_CLEAR_SEQ);
                    System.out.println("--------------------");
                    System.out.println("| Ваш баланс: " + isLoggedIn.split("_")[1]+" |");
                    System.out.println("--------------------");
                    System.out.println("Наберите номер одного из действий: ");
                    System.out.println("0 Заплатить");
                    System.out.println("1 Поменять пинкод");
                    System.out.println("2 Выйти");
                    userAnswer = read.nextInt();
                    if (userAnswer == 0) {
                        System.out.println(ANSI_CLEAR_SEQ);
                        read.nextLine();
                        System.out.println("Выберите пользователя, которому перейдет платеж:");
                        out.println("1_" + "u_" + username + "_" + pincode.hashCode() + "_" + "view");
                        for (String user: in.readLine().split("_")) System.out.println(user);
                        System.out.println("Или наберите 2, чтобы выйти");
                        String userToPay = read.nextLine();
                        if("2".equals(userToPay)) continue;
                        System.out.println("Сколько денег вы собираетесь перевести?");
                        String sum = read.nextLine();
                        out.println("1_" + "u_" + username + "_" + pincode.hashCode() + "_" + "pay_" + userToPay + "!" + sum);
                        String isOperationComplete = in.readLine();
                        if ("true".equals(isOperationComplete.split("_")[0])) {
                            System.out.println("Перевод прошел успешно. Ваш баланс: " + isOperationComplete.split("_")[1]);
                        } else {
                            System.out.println("Операция провалилась. Может, вы неправильно указали имя перевода или неверную сумму?");

                        }
                    }
                    else if (userAnswer == 1) {
                        System.out.println(ANSI_CLEAR_SEQ);
                        System.out.println("Введите новый пинкод или наберите 2, чтобы выйти:");
                        int newPincode = read.nextInt();
                        if (newPincode == 2){ userAnswer = 2; continue; }
                        out.println("1_" + "u_" + username + "_" + pincode.hashCode() + "_" + "change_" + newPincode);
                        String isOperationComplete = in.readLine();
                        if ("true".equals(isOperationComplete)) System.out.println("Операция прошла успешно");
                        else System.out.println("Операция провалилась. ");
                    }
                }
                out.println("out");
            }
            else {
                System.out.println("Вы не вошли. Может, неверный пинкод? Или вы новый пользователь?");
                System.out.println("Если хотите зарегестироваться, перейдите к банкомату со встроенным сканером ");
            }



        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
