package monopolybank;

import java.io.Serializable;

public abstract class MonopolyCode implements Serializable {
    private String description;
    private int id;

    private Terminal terminal;

    public MonopolyCode (int id,String description,Terminal terminal){
        this.description = description;
        this.id = id;
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return super.toString();
    }



    public int getId(){return id;}

    public String getDescription(){return description;}

    public Terminal getTerminal(){return terminal;}

    public abstract void doOperation(Player p);


}
