package client.model;

/**
 * Created by Future on 2/10/17.
 */
public class Food extends Entity{
    private int remainingTurn;

    Food(int id, int currentTurn) {
        super(id, EntityType.Food);
        remainingTurn = currentTurn--;
    }

    public int getRemainingTurns(){
        return remainingTurn;
    }
}
