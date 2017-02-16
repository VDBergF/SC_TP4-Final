import java.io.*;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.InflaterInputStream;

/**
 * Created by berg on 06/02/17.
 */
public class Commands {
    public static String lvlShell;
    public static String currentDir = System.getProperty("user.home") + "/";
    public static String showName;
    public static boolean isDirHome = true;

    static {
        try {
            lvlShell = "~";
            showName = System.getProperty ("user.name") + "@" + InetAddress.getLocalHost().getHostName() + ":";
        } catch (Exception e){
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
        } catch (IOException ex) {
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

    public void cd(String d, boolean isBack) {

        File dir = null;
        if (d.equals("/")) dir = new File(d);
        else if (d.equals("~")) dir = new File(System.getProperty("user.home") + "/");
        else {
            if (d.charAt(d.length()-1) != '/') d += "/";
            if (isBack) dir = new File(d);
            else dir = new File(currentDir + d);
        }

        if(dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
            if (d.equals("/")) {
                currentDir = d;
                lvlShell = "/";
                isDirHome = false;
            } else if (d.equals("~")) {
                currentDir = System.getProperty("user.home") + "/";
                lvlShell = "~";
                isDirHome = true;
            } else {
                if (!isBack) {
                    currentDir += d;
                    lvlShell += lvlShell.equals("/")? d.substring(0, d.length()-1): "/" + d.substring(0, d.length()-1);
                    if (lvlShell.equals(System.getProperty("user.home"))) lvlShell = "~";
                } else {
                    currentDir = d;
                    if (lvlShell.equals("~")) {
                        int cut = currentDir.lastIndexOf("/");
                        lvlShell = currentDir.substring(0, cut);
                    } else {
                        int cut = lvlShell.lastIndexOf("/");
                        lvlShell = lvlShell.substring(0, cut);
                    }
                }
            }
        } else {
            System.out.println(d + " is not a directory.");
        }
    }

    public void cdBack() {
        if (!currentDir.equals("/")) {
            int cutDir = currentDir.substring(0, currentDir.length()-1).lastIndexOf("/");
            if (cutDir == 0) cd("/", true);
            else cd(currentDir.substring(0,cutDir), true);
        }
    }

    public void clear() {
        for (int i = 0; i < 27; i++) {
            System.out.println("\n");
        }
    }

    public String pwd() {
        if (!currentDir.equals("/")) return currentDir.substring(0, currentDir.length()-1);
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