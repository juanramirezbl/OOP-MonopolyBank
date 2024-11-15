package monopolybank;



public abstract class Property extends MonopolyCode {

    private int price;

    private Player owner;

    private boolean mortgaged;

    private int mortgageValue;

    public Property (int id,String desc, Terminal terminal,int price,int mortgageValue){
        super(id,desc,terminal);
        this.price = price;
        this.mortgaged = false;
        this. mortgageValue = mortgageValue;
        this.owner = null;
    }

    public abstract int getPaymentForRent();

    public abstract int getPaymentForRentIfMortgaged();

    public Player getOwner(){return this.owner;}

    public void  setOwner(Player owner){
        this.owner = owner;
    }
    @Override
    public void doOperation(Player player){
        if (this.owner == null){
            getTerminal().showln("Se va a realizar la compra de la propiedad libre "+this.getDescription()+" por parte de "+player.getName()+"("+player.getColor()+") por un valor de "+this.price+"€ [1.Aceptar/2.Cancelar].");
            int option = getTerminal().readInt();
            if (option == 1){
                if(player.pay(price,false)){
                    player.addProperty(getId(),this);
                    setOwner(player);
                    showBuyPropertySummary(player);
                }else{
                    getTerminal().showln("El jugador no tiene suficiente dinero para comprar la propiedad.");
                }
            }else{
                getTerminal().showln("La operación ha sido cancelada.");
            }
        }else if(getOwner().equals(player)){ //si es mi propiedad ,hago operaciones en ella.
                doOwnerOperation();

        }else{//caer en una propiedad de otro jugador

            if (!this.mortgaged){
                int payment = getPaymentForRent();
                showPaymentSummary(payment,player);
                int option = getTerminal().readInt();

                if (option == 1){
                    if(player.pay(payment,true)){
                        getOwner().setBalance(getOwner().getBalance()+payment);
                        getTerminal().showln("El pago del alquiler se ha realizado correctamente");
                    }else {
                        getTerminal().showln("El jugador " + player.getName() + " (" + player.getColor() + ") no tiene suficiente dinero para pagar el alquiler.");
                        player.setBankrupt(true);
                        player.traspaseProperties(getOwner());

                    }
                }

            }else{
                int payment = getPaymentForRentIfMortgaged();// ejercicio 1 ordinaria
                showPaymentSummary(payment,player);
                int option = getTerminal().readInt();

                if (option == 1){
                    if(player.pay(payment,true)){
                        getOwner().setBalance(getOwner().getBalance()+payment);
                        getTerminal().showln("El pago del paso por una propiedad hipotecada se ha realizado correctamente");
                    }else {
                        getTerminal().showln( player.getName() + " (" + player.getColor() + ") no tiene suficiente dinero para pagar");
                        player.setBankrupt(true);
                        player.traspaseProperties(getOwner());

                    }
                }
            }

        }

    }


    public abstract void doOwnerOperation();



    private void showBuyPropertySummary(Player player){
        getTerminal().showln("La propiedad ha sido comprada por "+player.getName()+" ("+player.getColor()+") correctamente.");
    }


    public abstract void showPaymentSummary(int amount ,Player player);

    public boolean isMortgage(){
            return this.mortgaged;
    }

    public int getMortgageValue(){
        return this.mortgageValue;
    }

    public void setMortgaged(boolean bool){
        this.mortgaged = bool;
    }
}











