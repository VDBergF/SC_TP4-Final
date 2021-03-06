import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by berg on 06/02/17.
 */
public class Commands {
    private static String currentDir;
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

    LinkedList<String> listCommands() {
        LinkedList<String> list = new LinkedList<>();

        list.add("-> pwd");
        list.add("-> cat");
        list.add("-> ls");
        list.add("-> cd");
        list.add("-> touch");
        list.add("-> exit");
        list.add("-> clear");
        list.add("-> rm");
        list.add("-> mkdir");
        list.add("-> grep");
        list.add("-> | (pipe)");
        list.add("-> > (redirect)");
        list.add("-> mv");
        list.add("-> exec program params ...");

        return list;
    }

    LinkedList<String> cat(String[] files){
        LinkedList<String> result = new LinkedList<>();
        for (String file : files){
            result.addAll(cat(file));
        }
        return result;
    }

    LinkedList<String> exec(String[] program_params){
        Process process = null;

        try {
            program_params[0] = absPathFromRelativePath(program_params[0]);
            process = new ProcessBuilder(program_params).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        LinkedList<String> output = new LinkedList<>();

        try {
            while ((line = br.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    LinkedList<String> cat(String file) {
        if (file == null || file.isEmpty()) {
            System.out.println("cat: falta operando");
            return new LinkedList<>();
        }

        file = absPathFromRelativePath(file);
        LinkedList<String> strs = new LinkedList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            while((line = in.readLine())!= null){
                strs.add(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(file+", file not found.");
        } catch (IOException ex) {
            System.out.println(file+", input/output error.");
        }
        return strs;
    }

    LinkedList<String> grep(String file, String pattern){
        return grep(cat(file), pattern);
    }

    LinkedList<String> grep(LinkedList<String> file, String pattern){

        LinkedList<String> matches = new LinkedList<>();

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        Matcher m;


        for (String str : file){
            m = r.matcher(str);
            if(m.find()){
                matches.add(str);
            }
        }

        return matches;
    }

    LinkedList<String> ls() {
        LinkedList<String> ls = new LinkedList<>();

        File dir = new File(currentDir);

        String childs[] = dir.list();

        for(String child: childs){
            if(!child.startsWith(".")){
                ls.add(child);
            }
        }

        return ls;
    }

    LinkedList<String> cd(String d) {
        if (d == null || d.isEmpty()) d = System.getProperty("user.home");

        d = absPathFromRelativePath(d);
        if(new File(d).exists())
            currentDir = d;

        return new LinkedList<>();
    }

    LinkedList<String> touch(String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("touch: falta operando");
            return new LinkedList<>();
        }

        path = absPathFromRelativePath(path);

        File f = new File(path);

        if(f.exists()){
            System.out.println("Arquivo já existe");
            return new LinkedList<>();
        }

        f.getParentFile().mkdirs();

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LinkedList<>();
    }

    LinkedList<String> clear() {
        for (int i = 0; i < 27; i++) {
            System.out.println("\n");
        }
        return new LinkedList<>();
    }

    LinkedList<String> pwd() {
        LinkedList<String> lst = new LinkedList<>();
        lst.add(currentDir);
        return lst;
    }

    LinkedList<String> mkdir(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("mkdir: falta operando");
            return new LinkedList<>();
        }

        name = absPathFromRelativePath(name);
        try {
            File dir = new File(name);
            dir.mkdirs();
        } catch (Exception ex) {
            System.out.println("Erro ao criar o diretorio");
        }

        return new LinkedList<>();
    }

    LinkedList<String> rm(String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("rm: falta operando");
            return new LinkedList<>();
        }

        path = absPathFromRelativePath(path);
        File file = new File(path);

        rm(file);

        return new LinkedList<>();
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

    LinkedList<String> mv(String from, String to) {
        if (to == null || to.isEmpty()) {
            System.out.println("mv: falta operando");
            return new LinkedList<>();
        }

        // diretorio de origem
        from = absPathFromRelativePath(from);
        to = absPathFromRelativePath(to);

        File arq = new File(from);
        // diretorio de destino
        File dir = new File(to);
        // move o arquivo para o novo diretorio
        arq.renameTo(new File(dir, arq.getName()));

        return new LinkedList<>();
    }


    String absPathFromRelativePath(String path){

        // ja eh absoluto
        if(path.charAt(0) == '/') return path;

        // eh home
        if(path.charAt(0) == '~' || path.charAt(0) == '"') return home + path.substring(1);

        File a = new File(currentDir);
        File b = new File(a, path);
        String absolute = null;

        try {
            absolute = b.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return absolute;
    }

    String getDisplayInfo(){
        String pwd = currentDir;
        if(currentDir.startsWith(home)){
            pwd = currentDir.replace(home, "~");
        }
        return user + '@' + host + ':' + pwd + "$ ";
    }

    public String getHome() {
        return home;
    }
}