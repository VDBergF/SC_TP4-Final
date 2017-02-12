import java.io.*;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by berg on 06/02/17.
 */
public class Commands {
    public static String lvlShell;
    public static String currentDir = System.getProperty("user.home");

    static {
        try{
            lvlShell = System.getProperty ("user.name") + "@" + InetAddress.getLocalHost().getHostName() + ":~";
        }catch (Exception e){
            System.out.println("Exception caught ="+e.getMessage());
        }
    }

    public Commands() {
    }

    public String listCommands() {
        return "-> pwd\n-> cat\n-> ls\n-> cd\n-> cd ..\n-> exit\n-> clear\n-> rm -r\n-> mkdir\n-> mkdir -p\n-> mv";
    }

    public String cat(String file) {
        String r = "";
        try {
            FileReader fileReader = new FileReader(currentDir + "/" + file);
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
        File dir = new File(currentDir);
        String childs[] = dir.list();
        for(String child: childs){
            if (r.isEmpty()) r += child;
            else r += "\n" + child;
        }
        return r;
    }

    public String cd(String d) {
        File dir = new File(currentDir + "/" + d);
        if(dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
            currentDir += "/" + d;
            lvlShell += "/" + d;
            return d;
        } else {
            System.out.println(d + " is not a directory.");
        }
        return null;
    }

    public void cdBack() {
        int cut = lvlShell.lastIndexOf("/");
        int cutDir = currentDir.lastIndexOf("/");
        lvlShell = lvlShell.substring(0, cut);
        currentDir = currentDir.substring(0,cutDir);
    }

    public void clear() {
        for (int i = 0; i < 27; i++) {
            System.out.println("\n");
        }
    }

    public String pwd() {
        return currentDir;
    }

    public void mkdir(String name, boolean subdir) {
        try {
            File dir = new File(currentDir + "/" + name);
            if (subdir) dir.mkdirs();
            else dir.mkdir();
        } catch (Exception ex) {
            System.out.println("Erro ao criar o diretorio");
        }
    }

    public void rm(String path) {
        File file = new File(path);
        rm(file);
    }

    private void rm(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                rm(file);
            }
        }
        f.delete();
    }

    public void mv(String from, String to) {
        // diretorio de origem
        File arq = new File(currentDir + "/" + from);
        // diretorio de destino
        File dir = new File(to);
        // move o arquivo para o novo diretorio
        arq.renameTo(new File(dir, arq.getName()));
    }
}
