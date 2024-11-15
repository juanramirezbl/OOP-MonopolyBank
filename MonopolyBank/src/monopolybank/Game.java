package monopolybank;

import javax.imageio.ImageReadParam;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.*;

public class Game implements Serializable{

    private Map<Integer, MonopolyCode> monopolyCodeMap = new HashMap<>();

    private Map<Color, Player> players = new HashMap<>();

    private int numberOfPlayers;

    private int deposito;

    private Terminal terminal;


    public Game(Terminal terminal) {
        this.terminal = terminal;

    }

    private void loadMonopolyCodes() {
        String pathCodes = System.getProperty("user.dir") + File.separatorChar + "config" + File.separatorChar + "MonopolyCode.txt";
        File file = new File(pathCodes);

        Scanner s;

        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (s.hasNext()) {
            String[] casilla;
            String line = s.nextLine();
            casilla = line.split(";"); // cada campo separado por ; se convierte en un elemento del array casilla.
            switch (casilla[1]) {
                case "STREET":
                    Street street = new Street(casilla, terminal);
                    setMonopolyCodeMap(Integer.parseInt(casilla[0]), street);
                    break;

                case "TRANSPORT":
                    Transport transport = new Transport(casilla, terminal);
                    setMonopolyCodeMap(Integer.parseInt(casilla[0]), transport);
                    break;

                case "SERVICE":
                    Service service = new Service(casilla, terminal);
                    setMonopolyCodeMap(Integer.parseInt(casilla[0]), service);
                    break;

                case "REPAIRS_CARD":
                    RepairsCard repairsCard = new RepairsCard(casilla, terminal);
                    setMonopolyCodeMap(Integer.parseInt(casilla[0]), repairsCard);
                    break;

                case "PAYMENT_CHARGE_CARD":
                    PaymentCharge paymentCharge = new PaymentCharge(casilla, terminal,this);//Ejercicio 2 ordinaria.
                    setMonopolyCodeMap(Integer.parseInt(casilla[0]), paymentCharge);
            }
        }


    }


    public void setMonopolyCodeMap(int key, MonopolyCode mCode) {
        monopolyCodeMap.put(key, mCode);
    }

    public void play() {

        loadMonopolyCodes();

        createPlayers();

        gameMenu();


    }

    private void createPlayers() {
        List<Color> colors = new ArrayList<>(List.of(Color.values()));
        while (this.numberOfPlayers < 2 || this.numberOfPlayers > 4) {
            terminal.showln("Introduzca el número de jugadores que quiere crear (mínimo 2 y máximo 4):");
            this.numberOfPlayers = terminal.readInt();
            if (this.numberOfPlayers < 2 || this.numberOfPlayers > 4) {
                terminal.showln("Número de jugadores no válido. Inténtelo de nuevo.");
            }
        }

        this.players = new HashMap<>();

        // Crear jugadores
        for (int i = 0; i < numberOfPlayers; i++) {
            terminal.showln("Creación jugador " + (i + 1));
            int color = 0;
            while (color < 1 || color > (4 - i)) {
                terminal.showln("===========================");
                for (int j = 0; j < (4 - i); j++) {
                    terminal.showln("Pulse " + (j + 1) + " para el color " + colors.get(j));
                }
                terminal.show("Escoge un color: ");
                color = terminal.readInt();


                terminal.showln("Introduzca el nombre del jugador");
                String name = terminal.readString();


                Color strColor = colors.get(color - 1);
                Player newPlayer = new Player(strColor, name, terminal);
                players.put(strColor, newPlayer);
                colors.remove(color - 1);

                terminal.showln("================================");
                terminal.show("Jugador " + (i + 1) + " " + name + " " + strColor);
                terminal.showln("");


            }

        }

    }

    public void gameMenu() {
        int option = 5;
        try {
            while (this.numberOfPlayers > 1) { // bucle principal.
                do {
                    terminal.showln("============================");
                    terminal.showln("Pulse 1 para nueva operación");
                    terminal.showln("Pulse 2 para consultar estado de la partida");
                    terminal.showln("Pulse 3 para guardar la partida y salir");
                    terminal.showln("============================");

                    option = terminal.readInt();
                } while (option < 1 || option > 3);

                switch (option) {
                    case 1:
                        terminal.showln("Introduce el id de la carta:");
                        int id = terminal.readInt();
                        terminal.showln("Introduce la primera letra (en minúscula) del color del jugador (r, v, a, n)");
                        showPlayers(players);
                        char initial = terminal.readChar();
                        Color color = getColorFromInitial(initial);
                        if (color != null) {
                            if(id == 20){
                                    Player playerSelected = players.get(color);
                                    playerSelected.setBalance(playerSelected.getBalance() + deposito);
                                    setDeposito(0);
                                    terminal.showln("El jugador "+playerSelected.getColor()+" se lleva las ganancias del deposito.Ahora el deposito esta vacio.");

                            }else {
                                MonopolyCode square = monopolyCodeMap.get(id);
                                Player playerSelected = players.get(color);
                                doTransaction(square, playerSelected);
                            }
                        }else{
                            terminal.showln("Color no válido.Prueba otro.");
                        }
                        break;

                    case 2:
                        verEstadoPartida();
                        break;

                    case 3:
                        terminal.showln("Introduzca el nombre del archivo para guardar la partida:");
                        String fileName = terminal.readString();
                        try {
                            saveGame(fileName);
                            terminal.showln("Partida guardada correctamente.");
                            System.exit(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                            terminal.showln("Error al guardar la partida.");
                        }
                        break;

                    default:
                        terminal.showln("Opción no válida.");
                        break;
                }

                // Verificar si queda solo un jugador
                if (this.numberOfPlayers == 1) {
                    break;
                }
            }

            // Mensaje final del juego
            if (this.numberOfPlayers == 1) {
                Player winner = players.values().iterator().next();
                terminal.showln("El juego ha terminado. Solo queda un jugador.");
                terminal.showln("El ganador de la partida es el jugador " + winner.getName() + " (" + winner.getColor() + ").");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Color getColorFromInitial(char initial) {
        switch (initial) {
            case 'r':
                return Color.rojo;
            case 'v':
                return Color.verde;
            case 'a':
                return Color.azul;
            case 'n':
                return Color.negro;
            default:
                return null;
        }
    }
    public void showPlayers(Map<Color, Player> players) {
        terminal.showln("Jugadores disponibles:");
        for (Map.Entry<Color, Player> entry : players.entrySet()) {
            terminal.showln("Color: " + entry.getKey() + ", Jugador: " + entry.getValue().getName());
        }
    }




    public void doTransaction(MonopolyCode square, Player playerSelected) {
            square.doOperation(playerSelected);
            if (playerSelected.isBankrupt()) {
                removePlayer(playerSelected);
                oneLessPlayer();
            }

    }

    private void removePlayer(Player player) {
        this.players.remove(player.getColor());
    }

    private void verEstadoPartida() {
        for (Player player : players.values()) {
            terminal.showln("Player: " + player.getName());
            terminal.showln("Color: " + player.getColor());
            terminal.showln("Balance: " + player.getBalance());
            terminal.showln("Properties: ");
            for (Property property : player.getProperties().values()) {
                if (property instanceof Street street){
                    terminal.showln(" - " + property.getDescription()+" || Esta hipotecada?: "+property.isMortgage()+" || Numero casas: "+street.getBuiltHouses());

                }else{
                    terminal.showln(" - " + property.getDescription()+" || Esta hipotecada?: "+property.isMortgage());
                }

            }
            terminal.showln("Bankrupt: " + player.isBankrupt());
            terminal.showln("============================");
        }
        terminal.showln("Dinero actual en el deposito: "+getDeposito());

    }
    public void oneLessPlayer(){
        this.numberOfPlayers -= 1;
    }

    private String getFileName(String fileName) {
        return "config" + File.separatorChar + "oldGames" + File.separatorChar + fileName + ".bin";
    }

    public int getDeposito(){
        return this.deposito;
    }

    public void setDeposito(int money){
        this.deposito = money;
    }

    public void saveGame(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(getFileName(fileName));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

}
