import jssc.*;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientUsingRFID {

    private static Socket server;
    private final static String ANSI_CLEAR_SEQ = "\u001b[2J";
    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort("/dev/cu.usbmodem1411");
        Scanner read = new Scanner(System.in);
        try {
            //Открываем порт
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            System.out.print("Введите IP адрес:");
            String IP = read.nextLine();
            System.out.print("Введите порт: ");
            int port = read.nextInt();
            System.out.println(ANSI_CLEAR_SEQ);
            read.nextLine();
            while (true) {
                try {
                    InetAddress addr = InetAddress.getByName(IP);
                    server = new Socket(addr, port);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("Приложите, пожалуйста, Вашу карточку.");
                String rfid = "";
                String serialData = "";
                while (!serialData.equals("!")) {
                    serialData = serialPort.readString(1);
                    rfid += serialData;
                }
                rfid = rfid.replaceAll("!", "");
                action(rfid);
            }
        }
            catch(Exception e){
                e.printStackTrace();
            }
    }
    private static void action(String rfid) {
        try {
            Scanner read = new Scanner(System.in);
            PrintWriter out = new PrintWriter(new DataOutputStream(server.getOutputStream()), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            // Описать все по протоколу.
            System.out.print("Введите ваш пинкод:");
            String pincode = Integer.toString(read.nextInt());
            String out_data = "0_in_c_" + rfid+"_" + pincode;
            System.out.println("out_data: " + out_data);
            out.println(out_data.replaceAll("\n", ""));
            String isOperationComplete = in.readLine();
            System.out.println("Ответ сервера: " + isOperationComplete);
            int userAnswer = 3;
            if ("true".equals(isOperationComplete.split("_")[0])) {
                while(userAnswer != 2) {
                    System.out.println(ANSI_CLEAR_SEQ);
                    System.out.println("--------------------");
                    System.out.println("| Ваш баланс: " + isOperationComplete.split("_")[1]+" |");
                    System.out.println("--------------------");
                    System.out.println("Наберите номер одного из действий: ");
                    System.out.println("0 Заплатить");
                    System.out.println("1 Поменять пинкод");
                    System.out.println("2 Выйти");
                    userAnswer = read.nextInt();
                    if (userAnswer == 0) {
                        System.out.println(ANSI_CLEAR_SEQ);
                        read.nextLine();
                        System.out.println("--------------------");
                        System.out.println("| Ваш баланс: " + isOperationComplete.split("_")[1]+" |");
                        System.out.println("--------------------");
                        System.out.println("Выберите пользователя, которому перейдет платеж:");
                        out.println("1_" + "c_" + rfid + "_" + pincode + "_" + "view");
                        for (String user: in.readLine().split("_")) System.out.println(user);
                        System.out.println("Или наберите 2, чтобы выйти");
                        String userToPay = read.nextLine();
                        if("2".equals(userToPay)) continue;
                        System.out.println("Сколько денег вы собираетесь перевести?");
                        String sum = read.nextLine();
                        out.println("1_c_" + rfid + "_" + pincode + "_pay_" + userToPay + "!" + sum);
                        isOperationComplete = in.readLine();
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
                        if (newPincode == 2){  continue; }
                        out.println("1_c_" + rfid + "_" + pincode + "_change_" + newPincode);
                        isOperationComplete = in.readLine();
                        if ("true".equals(isOperationComplete)) System.out.println("Операция прошла успешно");
                        else System.out.println("Операция провалилась. ");
                    }
                }
                System.out.println(ANSI_CLEAR_SEQ);
                out.println("out");
            }
            else {
                System.out.println("Вы не вошли. Может, неверный пинкод? Или вы новый пользователь?");
                System.out.println("Если хотите зарегестироваться, напишите 1 ");
                System.out.println("Если хотите выйти, напишите любую другую цифру");
                userAnswer = read.nextInt();
                if(userAnswer == 1) {
                    read.nextLine();
                    System.out.print("Введите ваше имя:");
                    String userName = read.nextLine();
                    System.out.print("Введите пинкод еще раз: ");
                    String pincode1 = read.nextLine();
                    while(!pincode.equals(pincode1)) {
                        System.out.println("Пинкоды не совпали.");
                        System.out.println("Введите ваш пинкод:");
                        pincode = read.nextLine();
                        System.out.println("Введите пинкод еще раз: ");
                        pincode1 = read.nextLine();
                    }
                    System.out.println("Отрпавляю запрос на создание нового пользователя...");
                    out.println("0_up_"+userName+ "_"+rfid+"_"+pincode);
                    if("true".equals(in.readLine().split("_")[0])) {
                        System.out.println("Регистрация прошла успешно!");
                        out.println("out");
                    } else {
                        System.out.println("Регистрация провалилась");
                        out.println("out");
                    }
                }
                else  { out.println("out"); return; }
            }



        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
