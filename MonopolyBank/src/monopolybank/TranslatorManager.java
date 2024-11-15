package monopolybank;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TranslatorManager implements Serializable {

    private static final String DEFAULT_IDIOM = "spanish";

    public static final Map<String, String> IDIOMS = new HashMap<>();
    static {
        IDIOMS.put("spanish", "Español");
        IDIOMS.put("english", "English");
        IDIOMS.put("catalan", "Català");
        IDIOMS.put("euskera", "Euskara");
    }

    private Translator currentIdiom;
    private final Map<String, Translator> translators;


    public TranslatorManager() {
        translators = new TreeMap<>();
        for (String idiom : IDIOMS.keySet()) {
            try {
                translators.put(idiom, new Translator(idiom));
            } catch (IOException ex) {
                System.out.println("[TranslatorManager]: An error occured while loading '" + idiom + "' file");
            }
        }
        currentIdiom = translators.get(DEFAULT_IDIOM);
    }



    public void changeIdiom(String idiom) {
        currentIdiom = translators.get(idiom);
    }

    public String translate(String msg) {
        return currentIdiom.translate(msg);
    }

}
