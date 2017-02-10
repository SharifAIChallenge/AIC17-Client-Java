package client.model;

public class Slipper extends Entity {
    private int remainingTurn;
    Slipper(int id, int currentTurn) {
        super(id, EntityType.Slipper);
        remainingTurn = currentTurn--;
    }

    public int getRemainingTurns() {
        return this.remainingTurn;
    }
}