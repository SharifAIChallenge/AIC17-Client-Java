package client.model;

/**
 * Created by Future on 2/7/17.
 */
public class Information {
    private String contentLevel = "";        //"trash"-"food"-"fish"-""
    private int x;
    private int y;
    private int[] ids = {-1, -1, -1}; // content id-0, net id-1, teleport id-2
    private int contentValue = -1; // fish -0, food-1, trash-2
    private int direction = -1;
    private int color = -1;
    private int queen = -1;
    private int sick = -1;
    private int team = -1;
    private int targetId = -1;
    private boolean hasTeleport = false;
    private boolean hasNet = false;

    public void resetConstants(int id, int contentValue) {
        this.ids[0] = id;
        this.direction = -1;
        this.color = -1;
        this.queen = -1;
        this.sick = -1;
        this.team = -1;
        this.targetId = -1;
        this.contentLevel = "";
        this.contentValue = contentValue;
    }

    public void resetConstantsNet(int id) {
        this.ids[1] = id;
        this.direction = -1;
        this.color = -1;
        this.queen = -1;
        this.sick = -1;
        this.team = -1;
        this.targetId = -1;
        this.contentValue = -1;
    }

    public String getContentLevel() {
        return contentLevel;
    }

    public void setContentLevel(String contentLevel) {
        this.contentLevel = contentLevel;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public int getContentValue() {
        return contentValue;
    }

    public void setContentValue(int contentValue) {
        this.contentValue = contentValue;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getQueen() {
        return queen;
    }

    public void setQueen(int queen) {
        this.queen = queen;
    }

    public int getSick() {
        return sick;
    }

    public void setSick(int sick) {
        this.sick = sick;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public boolean isHasTeleport() {
        return hasTeleport;
    }

    public void setHasTeleport(boolean hasTeleport) {
        this.hasTeleport = hasTeleport;
    }

    public boolean isHasNet() {
        return hasNet;
    }

    public void setHasNet(boolean hasNet) {
        this.hasNet = hasNet;
    }
}
