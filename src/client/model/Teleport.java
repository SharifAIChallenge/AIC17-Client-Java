package client.model;

public class Teleport extends Entity{
    private Teleport pair;
    protected int targetId;

    Teleport(int id, int targetId) {
        super(id, EntityType.Teleport);
        this.targetId = targetId;
    }

    void setPair(Teleport pair) {
        this.pair = pair;
    }

    int getTargetId() {
        return targetId;
    }

    void setTargetId(int targetId) {
        this.targetId = targetId;
    }


    public Teleport getPair() {
        return pair;
    }


}