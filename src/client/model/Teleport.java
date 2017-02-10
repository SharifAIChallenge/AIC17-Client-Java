package client.model;

public class Teleport extends Entity{
    protected int targetId;

    Teleport(int id, int targetId) {
        this.targetId = targetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}