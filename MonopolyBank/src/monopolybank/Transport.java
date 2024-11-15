package monopolybank;

public class Transport extends Property{

    private int[] costStaying; // cuatro posibles costes de estancia 25,50,100,200

   //15;TRANSPORT;ESTACIÓN DE LAS DELICIAS;25;50;75;100;100

    public Transport (String [] configTextLine,Terminal terminal){
        super(Integer.parseInt(configTextLine[0]),configTextLine[2],terminal,(2*Integer.parseInt(configTextLine[7])),Integer.parseInt(configTextLine[7]));
        this.costStaying = new int[4];
        this.costStaying[0] = Integer.parseInt(configTextLine[3]);
        this.costStaying[1] = Integer.parseInt(configTextLine[4]);
        this.costStaying[2] = Integer.parseInt(configTextLine[5]);
        this.costStaying[3] = Integer.parseInt(configTextLine[6]);
    }

    @Override
    public int getPaymentForRent(){
        return costStaying[getOwner().getNumberOfTransport() - 1];
    }

    public int getPaymentForRentIfMortgaged(){
        getTerminal().showln("Como esta hipotecada pagarás un tercio del precio normal");
        getTerminal().showln("Precio normal: "+costStaying[getOwner().getNumberOfTransport()-1]);
        getTerminal().showln("Precio descontado a un tercio: "+(costStaying[getOwner().getNumberOfTransport()-1])/3);
        return (costStaying[getOwner().getNumberOfTransport()-1])/3;
    }

    @Override
    public void doOwnerOperation() {

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
                    "por un importe de "+getMortgageValue()+ " € [1.Aceptar/2.Cancelar]");
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
        getTerminal().show("El jugador " + player.getColor() + " usará el transporte " + getDescription() + " con " + getOwner().getNumberOfTransport() +
                " cartas de transporte. Por ello, pagará " +amount+ " € al jugador " +
                getOwner().getColor() + ". [1.Aceptar/2.Cancelar]");
    };





}
