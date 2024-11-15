package monopolybank;

public class RepairsCard extends MonopolyCode {

    private  int amountForHouse;

    private int amountForHotel;

    public RepairsCard(String [] configTextLine, Terminal terminal) {
        super(Integer.parseInt(configTextLine[0]),configTextLine[2],terminal);
        this.amountForHouse = Integer.parseInt(configTextLine[3]);
        this.amountForHotel = Integer.parseInt(configTextLine[4]);
    }

    @Override
    public void doOperation(Player p){
        getTerminal().showln(getDescription());
        getTerminal().showln("El jugador "+p.getColor()+" pagará "+this.amountForHouse+"€  por cada  casa y "+this.amountForHotel+"€ por cada hotel a la banca [1.Aceptar,2.Cancelar]");
        int option = getTerminal().readInt();
        if (option == 1) {
            int totalAmount = (this.amountForHotel * p.getNumberOfHotel()) + (this.amountForHouse * p.getNumberOfHouses());
            if( p.pay(totalAmount,true)){
                showSummary(p,totalAmount);
            }else{
                getTerminal().showln("El jugador no tiene suficiente dinero para pagarle a la banca");
                p.setBankrupt(true);
            }
        }else{
            getTerminal().showln("la operación ha sido cancelada");
        }

    }



    public void showSummary (Player p,int amount){
        getTerminal().showln(p.getName()+" ("+p.getColor()+") ha pagado "+amount+"€ a la banca");

    }


}
