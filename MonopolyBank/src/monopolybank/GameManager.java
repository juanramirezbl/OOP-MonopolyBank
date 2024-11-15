package monopolybank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.InputMismatchException;
import java.util.Map;

public class GameManager {

    private Terminal terminal = new TextTerminal();
    public void start(){
        int option = 0;
        boolean validOption = true;

        while (validOption) {
            try {
                terminal.showln("=====================");
                terminal.showln("PULSE 1 PARA SELECCIONAR IDIOMA");
                terminal.showln("PULSE 2 PARA INICIAR NUEVA PARTIDA");
                terminal.showln("PULSE 3 PARA REANUDAR UNA PARTIDA");
                terminal.showln("PULSE 4 PARA SALIR DEL SISTEMA");
                terminal.showln("=====================");
                option = terminal.readInt();


                if(option >= 1 && option <= 4){
                    validOption = false;
                }else{
                    terminal.showln("La opción introducida no es correcta.");
                }
            } catch (InputMismatchException e) {
                terminal.showln("Error: Ingresa un número.");
            }
        }

        switch (option){
            case 1:
                showIdioms(terminal);
                int seleccion = seleccionIdioma(terminal);
                String idiom = (String)TranslatorManager.IDIOMS.keySet().toArray()[seleccion - 1];
                terminal.getTranslatorManager().changeIdiom(idiom);
                terminal.show("Has escogido el ");
                terminal.showln("" + idiom);
                start();
                break;

            case 2:
                Game game = new Game(terminal);
                game.play();
                break;
            case 3:
                loadGameOption(terminal);
                break;
            case 4:
                System.exit(0);
        }
    }
    private void loadGameOption(Terminal terminal) {
        if (numberOfOldGames() == 0) {
            terminal.showln("No hay partidas guardadas.");
            start();
            return;
        }
        showOldGames(terminal);
        terminal.show("Selecciona el numero correspondiente a la partida que quieres cargar.");
        terminal.show(": ");
        int index = terminal.readInt();
        if (index > 0 && index <= numberOfOldGames()) {
            Game game = loadGame(index - 1);
            if (game != null) {
                game.gameMenu();
            } else {
                terminal.showln("Error al cargar la partida");
            }
        } else {
            terminal.showln("Opción inválida");
        }
    }
    private void showOldGames(Terminal terminal) {
        File folder = new File("config" + File.separatorChar + "oldGames" );
        int index = 1;
        for (File file : folder.listFiles()) {
            terminal.showln(index + ": " + file.getName());
            index++;
        }
    }

    private int numberOfOldGames() {
        File folder = new File("config" + File.separatorChar + "oldGames" );
        return folder.listFiles().length;
    }

    private Game loadGame(int index) {
        File folder = new File("config" + File.separatorChar + "oldGames" );
        File file = folder.listFiles()[index];
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Game)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private void showIdioms(Terminal terminal) {
        int index = 1;
        for (Map.Entry<String, String> entry : TranslatorManager.IDIOMS.entrySet()) {
            terminal.showln(index++ + ". " + entry.getValue());
        }
    }

    private int seleccionIdioma(Terminal terminal){
        int seleccion = -1;
        while (seleccion < 1 || seleccion > (TranslatorManager.IDIOMS.size())) {
            terminal.show("Escoge un idioma: ");
            seleccion = terminal.readInt();
        }
        return seleccion;
    }
}
