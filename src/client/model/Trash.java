package client.model;

/**
 * Created by Future on 2/10/17.
 */
public class Trash extends Entity {
    private int remainingTurn;
    Trash(int id, int currentTurn) {
        super(id, EntityType.Trash);
        this.remainingTurn = currentTurn--;
    }

    public int getRemainingTurns(){
        return this.remainingTurn;
    }
}
