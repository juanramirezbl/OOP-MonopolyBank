package monopolybank;

/**
 *
 * @author 
 */
public class MonopolyBank {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // El fichero con los codigos se encuentra en "config/MonopolyCode.txt"
        // Los idiomas deben estar en la carpeta "config/languages/"
        // Las partidas antiguas deberán estar en la carpeta "config/oldGames/"

        Terminal terminal = new TextTerminal();

        terminal.showln("BIENVENIDO AL MONOPOLY");

        GameManager gameM = new GameManager();
        gameM.start();

    }


}
