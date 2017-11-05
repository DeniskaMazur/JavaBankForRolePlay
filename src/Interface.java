import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

public class Interface extends JFrame{

    private static final Font normalFont = new Font("Serif", Font.BOLD, 15);
    private static final Font bigFont = new Font("Serif", Font.BOLD, 30);
    private static final Font extraBigFont = new Font("Serif", Font.BOLD, 100);

    public Interface(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bank");
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        addComponentsToPane(getContentPane());
    }

    private void checkLogIn(String name, int balance, String password){
        clear();
        String line = "false_0";

        //Обращение к API
        /*try {
            Socket s = new Socket(InetAddress.getByName("192.168.42.134"), 1912);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println("0_in_u_" + name + "_" + password);
            out.flush();
            while (!in.ready()){}
            line = in.readLine();
        }catch (IOException e){e.printStackTrace();}*/

        drawAccountStage(name, balance, getContentPane());
        refresh();
    }

    public void drawAccountStage(String name, int balance, Container pane){

        clear();

        pane.setLayout(new GridBagLayout());
        GridBagConstraints manager = new GridBagConstraints();

        JLabel hi = new JLabel("Ваш балнс: " + Integer.toString(balance));
        JButton pay = new JButton("Совершить перевод.");
        JButton change = new JButton("Сменить пароль.");
        JButton out = new JButton("Выйти.");


        hi.setFont(bigFont);
        pane.add(hi, manager);
        manager.ipadx = 80;
        manager.gridy = 1;
        manager.insets = new Insets(10, 0, 10, 0);
        pane.add(pay, manager);
        manager.gridy = 2;
        manager.insets = new Insets(10, 0, 10, 0);
        manager.ipadx = 101;
        pane.add(change, manager);
        manager.gridy = 3;
        manager.insets = new Insets(10, 0, 10, 0);
        manager.ipadx = 160;
        pane.add(out, manager);
        refresh();

        pay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPayStage(getContentPane());
            }
        });
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawChangeStage(getContentPane());
            }
        });
        out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outStage();
            }
        });

    }

    private void addComponentsToPane(Container pane) {

        clear();

        pane.setLayout(new GridBagLayout());
        GridBagConstraints manager = new GridBagConstraints();

        HintJTextField name = new HintJTextField(" введите ваш пароль");
        PasswordHintTextField pass = new PasswordHintTextField();
        JButton in = new JButton("LOG IN");
        JLabel username = new JLabel("ИМЯ: ");
        JLabel password = new JLabel("ПАРОЛЬ: ");
        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("res/logo.png")));

        manager.gridx = 1;
        pane.add(logo, manager);
        manager.gridx = 0;
        manager.gridy = 1;
        manager.anchor = GridBagConstraints.EAST;
        pane.add(username, manager);
        manager.gridy = 2;
        pane.add(password, manager);
        manager.gridy = 1;
        manager.gridx = 1;
        manager.anchor = GridBagConstraints.CENTER;

        manager.ipadx = 150;
        manager.ipady = 20;
        pane.add(name, manager);

        manager.gridy = 2;
        manager.ipadx = 185;
        manager.insets = new Insets(5, 0, 0, 0);
        manager.gridwidth = 2;
        pane.add(pass, manager);

        manager.gridy = 3;
        manager.gridwidth = 1;
        manager.ipadx = 40;
        manager.ipady = 20;
        manager.insets = new Insets(10, 0, 0, 0);
        pane.add(in, manager);

        refresh();

        pass.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                goIfEnter(e, name, pass);
            }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

        name.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                goIfEnter(e, name, pass);
            }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

        in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogIn(name.getText(), 100, String.valueOf(pass.getPassword()));
            }
        });

    }

    private void drawPayStage(Container pane){

        clear();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints manager = new GridBagConstraints();

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        String[] items = {"Иван", "Константин", "Ярослав", "Николай", "Женя", "Петр"};
        JComboBox users = new JComboBox(items);
        JFormattedTextField sum = new JFormattedTextField(formatter);
        JButton ok = new JButton("ОК");
        JLabel textOne = new JLabel("Перевести пользователю ");
        JLabel textTwo = new JLabel(" лордов.");
        JButton cancel = new JButton("Назад");

        textOne.setFont(normalFont);
        textTwo.setFont(normalFont);

        pane.add(textOne, manager);
        manager.gridx = 1;
        manager.insets = new Insets(10, 5, 10, 5);
        pane.add(users, manager);
        manager.gridx = 2;
        sum.setPreferredSize(new Dimension(100, 25));
        manager.insets = new Insets(10, 5, 10, 5);
        pane.add(sum, manager);
        manager.gridx = 3;
        pane.add(textTwo, manager);
        manager.gridy = 1;
        manager.gridx = 0;
        manager.gridwidth = 2;
        manager.anchor = GridBagConstraints.EAST;
        pane.add(cancel, manager);
        manager.gridx = 2;
        manager.anchor = GridBagConstraints.WEST;
        pane.add(ok, manager);

        refresh();

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sum.getText().equals(""))
                    drawAccountStage("иван", 90, pane);
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Перевод " + sum.getValue().toString() +
                        " лордов пользователю " + users.getSelectedItem().toString() + " прошел успешно.",
                            "ЧЕК", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawAccountStage("иван", 100, pane);
            }
        });

    }

    private void drawChangeStage(Container pane){

        clear();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints manager = new GridBagConstraints();

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(null);
        formatter.setMaximum(99999);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        JFormattedTextField newPass = new JFormattedTextField(formatter);
        JFormattedTextField concede = new JFormattedTextField(formatter);
        JButton change = new JButton("Сменить пароль");
        JButton cancel = new JButton("Назад");
        JLabel newLabel = new JLabel("Новый пароль: ");
        JLabel concedeLabel = new JLabel("Повторите пароль: ");

        newLabel.setFont(normalFont);
        concedeLabel.setFont(normalFont);
        newPass.setPreferredSize(new Dimension(150, 40));
        concede.setPreferredSize(new Dimension(150, 40));

        manager.anchor = GridBagConstraints.EAST;
        pane.add(newLabel, manager);
        manager.gridy = 1;
        pane.add(concedeLabel, manager);
        manager.anchor = GridBagConstraints.CENTER;
        manager.gridx = 1;
        manager.gridy = 0;
        pane.add(newPass, manager);
        manager.gridy = 1;
        manager.insets = new Insets(10, 0, 10, 0);
        pane.add(concede, manager);
        manager.gridx = 0;
        manager.gridy = 3;
        manager.anchor = GridBagConstraints.EAST;
        manager.insets = new Insets(0, 4, 0, 0);
        pane.add(cancel, manager);
        manager.gridx = 1;
        manager.anchor = GridBagConstraints.WEST;
        pane.add(change, manager);

        refresh();

        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newPass.getValue().toString().equals(concede.getValue().toString())){
                    drawAccountStage("иван", 100, getContentPane());
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Пароль сменен.");
                }
                else {
                    concedeLabel.setForeground(Color.RED);
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawAccountStage("иван", 100, getContentPane());
            }
        });

    }

    private void outStage(){
        int result = JOptionPane.showConfirmDialog(getContentPane(),
                "Вы точно хотите выйти.", "Выход",
                JOptionPane.YES_NO_OPTION);
        if (result == 0){
            addComponentsToPane(getContentPane());
        }
    }

    private void clear(){
        getContentPane().removeAll();
        repaint();
    }

    private void refresh(){
        setVisible(true);
    }

    private void goIfEnter(KeyEvent e, JTextField name, JTextField pass){
        if ((e.getKeyChar() == 10) &&
                !name.getText().equals("") &&
                !pass.getText().equals("")) checkLogIn(name.getText(), 100, pass.getText());
    }

}
