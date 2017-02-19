import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedList;

/**
 * Created by cassiano on 19/02/17.
 */
public class Logger {
    LinkedList<String> log;

    public Logger(){
        log = new LinkedList<>();
    }

    public void display(){
        for (String line : log){
            System.out.println(line);
        }
    }

    public void logCommand(String command){
        String data = getTime();

        log.add(data + "comando executado: " + command + "\n");
    }

    public void logBufferState(LinkedList<String> buffer){
        String data = getTime();

        String bufferState = "";

        for(String line : buffer){
            bufferState += line + "\n";
        }

        log.add(data + "estado do buffer:\n" + bufferState);
    }

    public String getTime(){
        return "[" + LocalDateTime.now().toString() + "] ";
    }

    public void saveToFile(String filename){
        Path file = Paths.get(filename);

        try {
            Files.write(file, log, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
