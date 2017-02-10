package client.model;

public class TeleportInformation extends Information {
    protected int targetId;

    TeleportInformation(int id, int targetId) {
        super(id);
        this.targetId = targetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}