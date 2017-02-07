import java.io.*;
import java.sql.Connection;

/**
 * Created by berg on 06/02/17.
 */
public class Commands {
    public static String lvlShell = System.getProperty ("user.name");
    public static String currentDir = System.getProperty("user.home");

    public Commands() {
    }

    public String listCommands() {
        return "-> pwd\n-> cat\n-> ls\n-> cd";
    }

    public String cat(String file) {
        String r = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            while((line = in.readLine())!= null){
                if (r.isEmpty()) r += line;
                else r += "\n" + line;
            }
        } catch (FileNotFoundException ex) {
            System.out.println(file+", file not found.");
        }
        catch (IOException ex) {
            System.out.println(file+", input/output error.");
        }
        return r;
    }

    public String ls() {
        String r = "";
        File dir = new File(System.getProperty("user.home"));
        String childs[] = dir.list();
        for(String child: childs){
            if (r.isEmpty()) r += child;
            else r += "\n" + child;
        }
        return r;
    }

    public String cd(String d) {
        File dir = new File(d);
        if(dir.isDirectory()) {
            System.setProperty("user.home", dir.getAbsolutePath());
            return d;
        } else {
            System.out.println(d + " is not a directory.");
        }
        return null;
    }
}
