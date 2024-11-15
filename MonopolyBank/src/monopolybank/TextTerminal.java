package monopolybank;

import java.util.Scanner;
public class TextTerminal extends Terminal{

    private transient Scanner scanner;

    @Override
    public int readInt(){
        scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    @Override
    public String readString() {
        scanner = new Scanner(System.in);
        return scanner.next();
    }
    @Override
    public char readChar(){
        scanner = new Scanner(System.in);
        return scanner.nextLine().charAt(0); //primer char de la linea.
    }

    @Override
    public void show(String string){
        System.out.println(getTranslatorManager().translate(string));
    }
    @Override
    public void showln(String string){
        System.out.println(getTranslatorManager().translate(string));
    }


}
