package client.model;

public class Teleport extends Entity{
    protected int targetId;
    private int remainingTurn;

    Teleport(int id, int targetId, int currentTurn) {
        super(id, EntityType.Teleport);
        this.targetId = targetId;
        this.remainingTurn = currentTurn--;
    }


    int getTargetId() {
        return targetId;
    }

    void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getRemainingTurns(){
        return this.remainingTurn;
    }
}