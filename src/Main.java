import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends JFrame {
    private JTextArea jTextArea;
    private static Commands commands = new Commands();
    private static Buffer buffer = new Buffer();
    private static Logger logger = new Logger();


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


        String input;
        String[] pipe_steps;
        String[] input_splitted;

        String p = "";

        while (true) {
            // mostra o console
            System.out.print(commands.getDisplayInfo());

            input = read.nextLine();

            logger.logCommand(input);

            // baguios de controle para o redirect '>'
            boolean saveToFile = false;
            String path_to_save = "";
            int last_element = 0;

            if(input.contains(">")){
                last_element = -1;
                saveToFile = true;
            }

            if(input.contains("|") || saveToFile){ // rolou um pipe ou redirect, saida precisa ser quebrada
                pipe_steps = input.split("[\\|>]");



                removeTrailingSpaces(pipe_steps);

                // se houve um redirect, o ultimo bagulho eh o caminho do arquivo para salvar o buffer
                for (int i = 0; i < pipe_steps.length + last_element; i++) {
                    logger.logCommand(pipe_steps[i]);
                    processCommand(pipe_steps[i].split(" "));
                    logger.logBufferState(buffer.getState());
                }

                if(saveToFile){
                    path_to_save = commands.absPathFromRelativePath(pipe_steps[pipe_steps.length - 1]);
                }

            }else {
                // o cara nao fez pipe nem redirect, so roda o comando
                input_splitted = input.split(" ");
                processCommand(input_splitted);
            }

            if(saveToFile){
                buffer.saveToFile(path_to_save);
            }else {
                System.out.println(buffer);
            }

            buffer.clear();
        }

    }

    private static void removeTrailingSpaces(String[] input){
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].trim();
        }
    }

    private static void processCommand(String[] input){
        removeTrailingSpaces(input);
        switch (input[0].toLowerCase()) {
            case "info":
                buffer.append(commands.listCommands());
                break;
            case "pwd":
                buffer.append(commands.pwd());
                break;
            case "cat":
                buffer.append(commands.cat(concatStr(input, 1).split(" ")));
                break;
            case "ls":
                buffer.append(commands.ls());
                break;
            case "cd":
                buffer.append(commands.cd(concatStr(input, 1)));
                break;
            case "clear":
                buffer.append(commands.clear());
                break;
            case "rm":
                buffer.append(commands.rm(concatStr(input, 1)));
                break;
            case "mkdir":
                buffer.append(commands.mkdir(concatStr(input, 1)));
                break;
            case "mv":
                buffer.append(commands.mv(input[1], input[2]));
                break;
            case "exit":
                logger.saveToFile("/home/cassiano/logsSC/log" + System.currentTimeMillis() % 100000 + ".txt");
                System.exit(0);
                break;
            case "grep":
                if(!buffer.isEmpty()){
                    buffer.append(commands.grep(buffer.read(), concatStr(input, 1)));
                }else{
                    buffer.append(commands.grep(input[1], concatStr(input, 2)));
                }
                break;
            case "touch":
                buffer.append(commands.touch(concatStr(input, 1)));
                break;
            default:
                System.out.println(input[0] + ": comando não encontrado");
                break;
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
