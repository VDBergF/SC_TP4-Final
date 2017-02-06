import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        System.out.println("Bem vindo ao prompt de comando!\nDigite 'info' para listar os comandos disponiveis" );

        Commands commands = new Commands();
        String[] input = read.nextLine().split(" ");

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
                    commands.cd(input[1]);
                    break;
                default:
                    break;
            }
            input = read.nextLine().split(" ");
        }
    }
}
