import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by cassiano on 16/02/17.
 */
public class Buffer {
    LinkedList<String> buffer;

    public Buffer(){
        clear();
    }

    public void append(LinkedList<String> data){
        buffer.addAll(data);
    }


    public LinkedList<String> read(){
        LinkedList<String> array = buffer;
        clear();
        return array;
    }

    public void clear(){
        buffer = new LinkedList<>();
    }

    @Override
    public String toString() {
        String result = "";

        for (String s: buffer){
            result += s + "\n";
        }

        return result;
    }

    public boolean isEmpty(){
        return buffer == null || buffer.size() == 0;
    }
}
