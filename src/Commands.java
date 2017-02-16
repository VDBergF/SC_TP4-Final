import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.InflaterInputStream;

/**
 * Created by berg on 06/02/17.
 */
public class Commands {
    private String currentDir;
    private String user;
    private String host;
    private String home;

    public Commands() {
        currentDir = home = System.getProperty("user.home");
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        user = System.getProperty ("user.name");

    }

    String listCommands() {
        return "-> pwd\n-> cat\n-> ls\n-> cd\n-> cd ..\n-> exit\n-> clear\n-> rm -r\n-> mkdir\n-> mkdir -p\n-> mv";
    }

    String cat(String[] files){
        String result = "";
        for (String file : files){
            result += cat(file);
        }
        return result;
    }

    String cat(String file) {
        String r = "";
        file = absPathFromRelativePath(file);
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
        } catch (IOException ex) {
            System.out.println(file+", input/output error.");
        }
        return r;
    }

    String ls() {
        String r = "";
        File dir = new File(currentDir);
        String childs[] = dir.list();
        for(String child: childs){
            if (r.isEmpty()) r += child;
            else r += !(child.startsWith(".")) ? "\n" + child : "";
        }
        return r;
    }

    void cd(String d) {
        d = absPathFromRelativePath(d);
        if(new File(d).exists())
            currentDir = d;
    }

    void clear() {
        for (int i = 0; i < 27; i++) {
            System.out.println("\n");
        }
    }

    String pwd() {
        return currentDir;
    }

    void mkdir(String name) {
        name = absPathFromRelativePath(name);
        try {
            File dir = new File(name);
            dir.mkdirs();
        } catch (Exception ex) {
            System.out.println("Erro ao criar o diretorio");
        }
    }

    void rm(String path) {
        path = absPathFromRelativePath(path);
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

    void mv(String from, String to) {
        // diretorio de origem

        from = absPathFromRelativePath(from);
        to = absPathFromRelativePath(to);

        File arq = new File(from);
        // diretorio de destino
        File dir = new File(to);
        // move o arquivo para o novo diretorio
        arq.renameTo(new File(dir, arq.getName()));
    }


    String absPathFromRelativePath(String path){

        //System.out.println("CURRENT -> " + currentDir);
        //System.out.println("DESTINATION -> " + path);

        // ja eh absoluto
        if(path.charAt(0) == '/') return path;

        File a = new File(currentDir);
        File b = new File(a, path);
        String absolute = null;
        try {
            absolute = b.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("ABSOLUTE -> " + absolute);
        return absolute;
    }

    String getDisplayInfo(){
        String pwd = currentDir;
        if(currentDir.startsWith(home)){
            pwd = currentDir.replace(home, "~");
        }
        return user + '@' + host + ':' + pwd + "$ ";
    }
}