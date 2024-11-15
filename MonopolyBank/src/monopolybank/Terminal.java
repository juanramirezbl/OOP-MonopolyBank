package monopolybank;

import java.io.Serializable;

public abstract class Terminal implements Serializable {
    private TranslatorManager translatorManager;

    public abstract int readInt();

    public abstract String readString();

    public abstract char readChar();

    public abstract void show(String string);

    public abstract void showln(String string);

    public Terminal(){
        translatorManager = new TranslatorManager();
    }

    public TranslatorManager getTranslatorManager(){
        return translatorManager;
    }





}
