package monopolybank;


public class Street extends Property{

    private int builtHouses;

    private int housePrice;

    private int[] costStayingWithHouses = new int[6];


    public Street (String [] configTextLine,Terminal terminal){
        super(Integer.parseInt(configTextLine[0]),configTextLine[2],terminal,Integer.parseInt(configTextLine[11])*2,Integer.parseInt(configTextLine[11]));
        this.builtHouses = 0;
        this.housePrice = Integer.parseInt(configTextLine[10]);
        for (int i = 3; i<=8; i++){
            costStayingWithHouses[i-3] = Integer.parseInt(configTextLine[i]);
        }
    }

    @Override
    public int getPaymentForRent() {
        return costStayingWithHouses[builtHouses];
    }

    public int getPaymentForRentIfMortgaged(){// ejercicio 1 ordinaria
        getTerminal().showln("Como esta hipotecada pagarás un tercio del precio normal");
        getTerminal().showln("Precio normal: "+costStayingWithHouses[builtHouses]);
        getTerminal().showln("Precio descontado a un tercio: "+(costStayingWithHouses[builtHouses])/3);
        return costStayingWithHouses[builtHouses]/3;
    }

    @Override
    public void doOwnerOperation() {
        Player p = getOwner();

        getTerminal().showln("Esta propiedad pertenece al jugador "+p.getColor()+ " de nombre "+p.getName());
        menuDoOwnerOperation();
        int option = getTerminal().readInt();
        switch (option){
            case 1:
                mortgageProperty(p);
                break;

            case 2:
                payOffMortgage(p);
                break;

            case 3:
                buyHouses(p);
                break;

            case 4:
                sellHouses(p);
                break;
        }


    }

    public void showPaymentSummary(int amount ,Player player){

        getTerminal().showln("El jugador "+player.getColor()+" usuará la propiedad "+getDescription()+ " con "+builtHouses+" casas.Por ello, pagará "+amount+
        " € al jugador "+getOwner().getColor()+"[1.Aceptar/2.Cancelar]");

    };

    private void showPurchaseSummary(int amount ,Player player){};


    public int getNumberOfHouses(){
        return this.builtHouses;
    }

    public void menuDoOwnerOperation(){
        getTerminal().showln("MENÚ PROPIETARIO");
        getTerminal().showln("PULSE 1 PARA HIPOTECAR PROPIEDAD");
        getTerminal().showln("PULSE 2 PARA DESHIPOTECAR PROPIEDAD");
        getTerminal().showln("PULSE 3 PARA COMPRAR CASAS EN LA PROPIEDAD");
        getTerminal().showln("PULSE 4 PARA VENDER CASAS DE LA PROPIEDAD");
    }

    public void mortgageProperty(Player p){
        if (builtHouses == 0 && !isMortgage()) {
            getTerminal().showln("Se va a realizar la hipoteca de la propiedad " + getDescription() +
                    " por parte del jugador " + p.getColor() + " por un importe de " + getMortgageValue() + " € [1.Aceptar/2.Cancelar]");
            int option = getTerminal().readInt();
            if (option == 1) {
                setMortgaged(true);
                p.setBalance(p.getBalance() + getMortgageValue());
                getTerminal().showln("La propiedad ha sido hipotecada correctamente.");
            } else {
                getTerminal().showln("La operación ha sido cancelada.");
            }
        }else if (isMortgage()){
            getTerminal().showln("Esta propiedad ya esta hipotecada.");
        }else{
            getTerminal().showln("No es posible hipotecar una propiedad si aún conserva casas u hoteles.Actualmente hay "+builtHouses+" casa/s");

        }
    }

    public void payOffMortgage(Player p) {
        if (isMortgage()) {
            getTerminal().showln("Se va a deshipotecar la propiedad " + getDescription() + " por parte del jugador " + p.getColor() +
                    "por un importe de " + getMortgageValue() + " € [1.Aceptar/2.Cancelar.]");
            int option = getTerminal().readInt();
            if (option == 1) {
                setMortgaged(false);
                p.setBalance(p.getBalance() - getMortgageValue());
                getTerminal().showln("La porpiedad ha sido deshipotecada correctamente.");
            } else {
                getTerminal().showln("La operación ha sido cancelada");
            }
        } else {
            getTerminal().showln("La propiedad no se puede deshipotecar porque no ha sido hipotecada previamente.");
        }
    }
    public int getBuiltHouses(){
        return this.builtHouses;
    }

    public void buyHouses(Player p) {
        int max = 5 - getBuiltHouses();
        if (!isMortgage() && builtHouses < 5) {
            getTerminal().showln("Actualmente hay " + getBuiltHouses() + " casa/s por tanto solo se pueden comprar " + max + " casa/s como maximo.");
            getTerminal().showln("Introduce el numero de casas que quieres comprar: ");
            int nHouses = getTerminal().readInt();
            if (nHouses <= max){
                int amount = nHouses * housePrice;
                getTerminal().showln("Se va a realizar la compra de "+nHouses+" casa/s para la propiedad "+getDescription()+
                        " por parte del jugador "+p.getColor()+" por un importe de "+ amount+" €[1.Aceptar/2.Cancelar].");
                int option = getTerminal().readInt();
                if (option == 1){
                    if (p.getBalance() > amount) {
                        p.setBalance(p.getBalance() - amount);
                        getTerminal().showln("Se han construido " + nHouses + " casas por un importe de " + amount + " €");
                        if (nHouses == 5) {
                            getTerminal().showln("Has construido un hotel.");
                        }
                        builtHouses += nHouses;
                    }else{
                        getTerminal().showln("No hay suficiente dinero para asumir la compra.");
                    }
                }else{
                    getTerminal().showln("La operación ha sido cancelada.");
                }
            }else{
                getTerminal().showln("Numero de casas no valido, se cancela la operación.");
            }
        }else if(isMortgage()){
            getTerminal().showln("Actualmente la propiedad esta hipotecada.");
        }else{
            getTerminal().showln("Actualmente hay " + getBuiltHouses() + " casas (1 hotel) por tanto, no se pueden comprar más casas.");
        }
    }

    public void sellHouses(Player p){
        if (!isMortgage() && builtHouses > 0){
            int option = -1;
            while (option < 0 || option > builtHouses){
                getTerminal().show("¿Cuántas casas deseas vender?(Actualmente construidas: "+builtHouses+")");
                option = getTerminal().readInt();
            }
            if(option == 0){
                return;
            }
            int opt = 0;
            while (opt != 1 && opt != 2){
                getTerminal().showln("Se va a realizar la venta de " + option +" casas para la propiedad" +
                        this.getDescription() + " por parte del jugador "+ getOwner().getColor() +" por un" +
                        "importe de "+ (housePrice * option) / 2+ " €. [1.Aceptar/2.Cancelar].");
                opt = getTerminal().readInt();
            }
            if(opt == 1){
                int transaction = (option * housePrice) / 2; // se venden por la mitad del precio de compra.
                p.setBalance(p.getBalance() + transaction);
                this.builtHouses -= option;
                getTerminal().showln("Las casas se han vendido correctamente vendido correctamente");

            }else{
                getTerminal().showln("La operación de comprar ha sido cancelada.");
            }
        }else if(isMortgage()){
            getTerminal().showln("Actualmente la propiedad esta hipotecada.");
        }else{
            getTerminal().showln("Actualmente hay " + getBuiltHouses() + " casas por tanto, no hay nada que vender.");

        }
    }



}
