import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        System.out.println("Bem vindo ao prompt de comando!\nDigite 'info' para listar os comandos disponiveis" );

        Commands commands = new Commands();
        System.out.print(Commands.lvlShell + "@:~$ ");

        String[] input = read.nextLine().split(" ");
        String p = "";
        while (!input[0].toLowerCase().equals("exit")) {
            switch (input[0].toLowerCase()) {
                case "info":
                    System.out.println(commands.listCommands());
                    break;
                case "pwd":
                    String pwd = System.getProperty("user.dir");
                    System.out.println(pwd);
                    break;
                case "cat":
                    System.out.println(commands.cat(input[1]));
                    break;
                case "ls":
                    System.out.println(commands.ls());
                    break;
                case "cd":
                    p = commands.cd(input[1]);
                    break;
                default:
                    break;
            }
            if (p == null || p.isEmpty())
                System.out.print(Commands.lvlShell + "@:~$ ");
            else
                System.out.print(Commands.lvlShell + "@:~/" + p + "$ ");

            input = read.nextLine().split(" ");
        }

    }
}
