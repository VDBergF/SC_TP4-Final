import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends JFrame {
    private JTextArea jTextArea;

    public Main() {
        super("Terminal");
        setSize(730, 410);
//        this.jTextArea = new JTextArea();
//        this.jTextArea.setLineWrap(true); //quebra de linha automática
//        add(this.jTextArea);
        Color c = Color.decode("#2C001E");
        getContentPane().setBackground(c);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

//        final Main t = new Main();
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                t.setVisible(true);
//            }
//        });

        String welcome = "Bem vindo ao prompt de comando!\nDigite 'info' para listar os comandos disponiveis";
        System.out.println(welcome);
//        t.jTextArea.setText(welcome);
//        t.add(t.jTextArea);

        Commands commands = new Commands();
        System.out.print(Commands.lvlShell + "$ ");

        String[] input = read.nextLine().split(" ");
        String p = "";
        while (true) {
            switch (input[0].toLowerCase()) {
                case "info":
                    System.out.println(commands.listCommands());
                    break;
                case "pwd":
                    System.out.println(commands.pwd());
                    break;
                case "cat":
                    System.out.println(commands.cat(concatStr(input, 1)));
                    break;
                case "ls":
                    System.out.println(commands.ls());
                    break;
                case "cd":
                    if (input[1].equals(".."))
                        commands.cdBack();
                    else p = commands.cd(concatStr(input, 1));
                    break;
                case "clear":
                    commands.clear();
                    break;
                case "rm":
                    if (input[1].equals("-r")) commands.rm(concatStr(input, 2));
                    break;
                case "mkdir":
                    if (input[1].equals("-p")) commands.mkdir(concatStr(input, 2), true);
                    else commands.mkdir(concatStr(input, 1), false);
                    break;
                case "mv":
                    commands.mv(input[1], concatStr(input, 2));
                    break;
                case "exit":
                    System.exit(0);
                default:
                    break;
            }
            if (p == null || p.isEmpty())
                System.out.print(Commands.lvlShell + "$ ");
            else
                System.out.print(Commands.lvlShell + "$ ");

            input = read.nextLine().split(" ");
        }

    }

    public static String concatStr(String[] input, int init) {
        String r = "";
        for (int i = init; i < input.length; i++) {
            if (r.isEmpty()) r += input[i];
            else r += " " + input[i];
        }
        return r;
    }
}
