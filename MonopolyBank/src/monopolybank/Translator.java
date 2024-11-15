package monopolybank;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Translator implements Serializable {


    private String language;
    private final Map<String, String> messages;

    public Translator(String language) throws IOException {
        this.language = language;
        this.messages = new TreeMap<>();

        read("config" + File.separatorChar + "languages" + File.separatorChar + this.language + ".txt");
    }

    private void read(String translatorFile) throws IOException {
        FileReader fr = new FileReader(translatorFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] slices = line.split(";");
            String msg = slices[0];
            String translation = slices[1];
            this.messages.put(msg, translation);
        }

        br.close();
    }

    public String translate(String msg) {
        String value = this.messages.get(msg);
        if (value != null) {
            return value;
        }
        return msg;
    }
}
