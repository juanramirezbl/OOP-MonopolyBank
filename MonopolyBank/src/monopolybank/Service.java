package monopolybank;



public class Service extends Property{

    private int[] costStaying;

    //12;SERVICE;COMPAÑÍA DE ELECTRICIDAD;4;10;75
    public Service (String [] configTextLine,Terminal terminal){
        super(Integer.parseInt(configTextLine[0]),configTextLine[2],terminal,(2 * Integer.parseInt(configTextLine[5])), Integer.parseInt(configTextLine[5]));
        this.costStaying = new int[2];
        this.costStaying[0] = Integer.parseInt(configTextLine[3]);
        this.costStaying[1] = Integer.parseInt(configTextLine[4]);
    }

    @Override
    public int getPaymentForRent(){
        getTerminal().showln("Introduce el numero obtenido en los dados");
        int dados = getTerminal().readInt();
        if (getOwner().getNumberOfServices() == 1){
            return costStaying[0] * dados;
        }else{
            return costStaying[1] * dados;
        }
    }

    public int getPaymentForRentIfMortgaged(){ // ejercicio 1 ordinaria.
        getTerminal().showln("Introduce el numero obtenido en los datos");
        int dados = getTerminal().readInt();
        if (getOwner().getNumberOfServices() == 1){
            getTerminal().showln("Como esta hipotecada pagarás un tercio del precio normal");
            getTerminal().showln("Precio normal: "+costStaying[0] * dados);
            getTerminal().showln("Precio descontado a un tercio: "+(costStaying[0] * dados)/3);
            return (costStaying[0] * dados)/3;
        }else{
            getTerminal().showln("Como esta hipotecada pagarás un tercio del precio normal");
            getTerminal().showln("Precio normal: "+costStaying[0] * dados);
            getTerminal().showln("Precio descontado a un tercio: "+(costStaying[0] * dados)/3);
            return (costStaying[1] * dados)/3;
        }
    }


    @Override
    public void doOwnerOperation() { // en servicio solo se puede o hipotecar o deshipotecar

        Player p = getOwner();

        getTerminal().showln("ESTA PROPIEDAD PERTENECE AL JUGADOR "+p.getColor()+ "DE NOMBRE "+p.getName());


        if (!isMortgage()){
            getTerminal().showln("La propiedad no esta hipotecada.");
            getTerminal().showln("Se va a realizar la hipoteca de la propiedad "+getDescription()+
                    " por parte del jugador "+p.getColor()+" por un importe de "+getMortgageValue()+" € [1.Aceptar/2.Cancelar]");
            int option = getTerminal().readInt();
            if (option == 1){
                setMortgaged(true);
                p.setBalance(p.getBalance() + getMortgageValue());
                getTerminal().showln("La propiedad ha sido hipotecada correctamente.");
            }else{
                getTerminal().showln("La operación ha sido cancelada.");
            }
        }else{
            getTerminal().showln("La propiedad esta hipotecada.");
            getTerminal().showln("Se va a deshipotecar la propiedad "+getDescription()+"por parte del jugador "+p.getColor()+
                    "por un importe de "+getMortgageValue()+ " € [1.Aceptar/2.Cancelar.]");
            int option = getTerminal().readInt();
            if (option == 1) {
                setMortgaged(false);
                p.setBalance(p.getBalance() - getMortgageValue());
                getTerminal().showln("La porpiedad ha sido deshipotecada correctamente.");
            }else{
                getTerminal().showln("La operación ha sido cancelada");
            }
        }

    }
    public void showPaymentSummary(int amount ,Player player){
        getTerminal().show("El jugador " + player.getColor() + " usará el servicio " + getDescription() + " con un propietario con " + getOwner().getNumberOfServices() +
                " cartas. Por ello, pagará " +amount+ " € al jugador " +
                 getOwner().getColor() + ". [1.Aceptar/2.Cancelar]");
    };


}
