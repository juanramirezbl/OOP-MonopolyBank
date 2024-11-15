package monopolybank;

public class PaymentCharge extends MonopolyCode{
    private int amount;

    private Game game;

    public PaymentCharge(String [] configTextLine,Terminal terminal,Game game){
        super(Integer.parseInt(configTextLine[0]),configTextLine[2],terminal);
        this.amount = Integer.parseInt(configTextLine[3]);
        this.game = game;
    }
    @Override
    public void doOperation(Player player){
        showSummary(player, amount);
        int option = getTerminal().readInt();
        if (option == 1) {
            if (amount < 0) {
                if (player.pay(-amount, true)) {
                    getTerminal().showln(  player.getName() + " (" + player.getColor() +
                            ") ha pagado " + -amount + "€ por <" + getDescription() + "> a la banca y se guardará en el deposito");
                    saveMoney(game,-amount);
                    return;
                } else {
                    getTerminal().showln(player.getName() + " (" + player.getColor() +
                            ") no tiene suficiente dinero para pagar " + -amount + " € por <" + getDescription() + "> a la banca.");
                }
            } else {
                player.setBalance(player.getBalance() + amount);
                return;
            }
        } else {
            getTerminal().showln("La operación de <" + this + "> ha sido cancelada");
        }
        player.setBankrupt(true);
    }


    private void showSummary(Player player ,int amount){
        if (amount < 0) {
            getTerminal().show(player.getName() + " (" + player.getColor() +
                    ") pagará " + -amount + "€ por <" + getDescription() + "> a la banca. [1.Aceptar/2.Cancelar]. ");
        } else {
            getTerminal().show( player.getName() + " (" + player.getColor() +
                    ") cobrará " + amount + "€ por <" + getDescription() + "> de la banca. [1.Aceptar/2.Cancelar]. ");
        }

    }

    public void saveMoney(Game game,int amount){// ejercicio 2 ordinaria.
        getTerminal().showln("Tus perdidas irán a parar al depostio ("+amount+")");
        game.setDeposito(game.getDeposito() + amount);
    }



}
