package monopolybank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Serializable {

    private Color color;

    private String name;

    private int balance;

    private HashMap<Integer,Property> properties;

    private Terminal terminal;

    private final TranslatorManager translatorManager;

    private boolean bankrupt;

    public Player (Color color,String name,Terminal terminal){
        this.color = color;
        this.name = name;
        this.balance =1500;
        this.bankrupt = false;
        this.properties = new HashMap<>();
        this.translatorManager = terminal.getTranslatorManager();
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Color getColor(){return this.color;}

    public String getName(){return this.name;}

    public int getBalance(){return this.balance;}

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addProperty(int id,Property property){
        this.properties.put(id,property);
    }

    public boolean pay(int amount,boolean mandatory){
        if (this.balance >= amount){
            this.balance -= amount;
            terminal.showln("El balance ahora es de: "+this.balance);
            return true;
        }else {
            this.balance -= amount;
            terminal.showln("El balance ahora es de: "+this.balance);
            if (mandatory){
                return sellActives(0);
            }
        }
        return false;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public HashMap<Integer, Property> getProperties() {
        return properties;
    }






    public boolean isBankrupt() {
        return bankrupt;
    }




    public int getNumberOfHouses(){ // NUM CASAS POR STREET
        int nHouses = 0;
        for(Property property : properties.values()){
            if (property instanceof Street){
                nHouses += ((Street) property).getNumberOfHouses();
            }
        }
        return nHouses;
    }

    public int getNumberOfHotel(){ // NUM HOTELES POR STREET (5 CASAS = 1 HOTEL)
        int nHotels = 0;
        for(Property property : properties.values()){
            if (property instanceof Street){
                Street street = (Street)property;
                if (street.getNumberOfHouses() == 5){
                    nHotels ++;
                }
            }
        }
        return nHotels;
    }
    public int getNumberOfServices() {
        int numberOfServices = 0;
        for (Property property : properties.values()) {
            if (property instanceof Service) {
                numberOfServices++;
            }
        }
        return numberOfServices;
    }

    public int getNumberOfTransport() {
        int numberOfTransport = 0;
        for (Property property : properties.values()) {
            if (property instanceof Transport) {
                numberOfTransport++;
            }
        }
        return numberOfTransport;
    }

    private ArrayList<Property> propertiesToSell() {
        ArrayList<Property> properties1ToSell = new ArrayList<>();
        for (Property property : properties.values()) {
            if (!property.isMortgage()) {
                properties1ToSell.add(property);
            }
        }
        return properties1ToSell;
    }



    private boolean sellActives(int target) {
        boolean continueSelling = true;
        terminal.showln("Necesitas vender activos para superar este aprieto.Balance actual: "+this.balance);
        while (continueSelling) {
            ArrayList<Property> propertiesToSell = propertiesToSell(); // Obtiene la lista de propiedades a vender
            boolean madeProgress = false; // Variable para verificar si se ha hecho algún progreso


            for (int i = 0; i < propertiesToSell.size(); i++) {
                Property property = propertiesToSell.get(i);
                String status = property.isMortgage() ? " (hipotecada)" : "";
                String houseInfo = "";
                if (property instanceof Street) {
                    houseInfo = " - Casas: " + ((Street) property).getBuiltHouses();
                }
                terminal.showln((i + 1) + ". " + property.getDescription() + houseInfo + status);
            }

            if (propertiesToSell.isEmpty()) {
                terminal.showln("No tienes más opciones para vender o hipotecar.");
                continueSelling = false;
                break;
            }
            terminal.showln("Elige una propiedad para vender casas o hipotecar:");
            int choice = getPlayerChoice(propertiesToSell.size());
            Property chosenProperty = propertiesToSell.get(choice - 1);

            if (chosenProperty instanceof Street street) {
                if (street.getBuiltHouses() > 0) {
                    terminal.showln("La propiedad " + chosenProperty.getDescription() + " tiene " + street.getBuiltHouses() + " casas construidas");
                    street.sellHouses(this);
                    madeProgress = true;
                } else if (!chosenProperty.isMortgage()) {
                    street.mortgageProperty(this);
                    madeProgress = true;
                }
            } else if(chosenProperty instanceof Service service) {
                    service.doOwnerOperation();

            }else if(chosenProperty instanceof Transport transport){
                transport.doOwnerOperation();
            }

            terminal.showln("Balance actualizado: " + balance + "€");

            if (balance >= target) {
                continueSelling = false;
            } else if (!madeProgress) {
                terminal.showln("No tienes más opciones para vender o hipotecar.");
                continueSelling = false;
            }
        }

        if (balance >= target) {
            balance -= target;
            return true;
        } else {
            terminal.showln("No has podido asumir las deudas, quedas en bancarrota (eliminado)");
            return false;
        }
    }



    private int getPlayerChoice(int maxChoice) {
        int choice = 0;
        while (choice < 1 || choice > maxChoice) {
            terminal.show("Introduce el número de la propiedad (1-" + maxChoice + "): ");
            try {
                choice = terminal.readInt();
            } catch (NumberFormatException e) {
                terminal.showln("Entrada inválida. Por favor, introduce un número.");
            }
        }
        return choice;
    }



    public void traspaseProperties(Player newOwner){
        for (Property property : properties.values()) {
            property.setOwner(newOwner);
            newOwner.properties.put(property.getId(), property);
        }
    }


}
