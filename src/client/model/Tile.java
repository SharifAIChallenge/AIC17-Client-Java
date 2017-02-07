package client.model;

import com.google.gson.JsonArray;

public class Tile {
    private String contentLevel;        //"trash"-"food"-"fish"-""
    private int x;
    private int y;
    private int[] ids = {-1, -1, -1}; // content id-0, net id-1, teleport id-2
    private int direction = -1;
    private int color = -1;
    private int queen = -1;
    private int sick = -1;
    private int team = -1;
    private int targetId = -1;
    private boolean hasTeleport = false;
    private boolean hasNet = false;


    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addFishInfo(JsonArray fishInfo) {
        ids[0] = fishInfo.get(0).getAsInt();
        direction = fishInfo.get(3).getAsInt();
        color = fishInfo.get(4).getAsInt();
        queen = fishInfo.get(5).getAsInt();
        sick = fishInfo.get(6).getAsInt();
        team = fishInfo.get(7).getAsInt();
        this.setContentLevel("fish");
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
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

    public String getContentLevel() {
        return contentLevel;
    }

    public void setContentLevel(String contentLevel) {
        this.contentLevel = contentLevel;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public void resetConstants(int id) {
        this.ids[0] = id;
        this.direction = -1;
        this.color = -1;
        this.queen = -1;
        this.sick = -1;
        this.team = -1;
        this.targetId = -1;
        this.contentLevel = "";
    }

    public void resetConstantsNet(int id) {
        this.ids[1] = id;
        this.direction = -1;
        this.color = -1;
        this.queen = -1;
        this.sick = -1;
        this.team = -1;
        this.targetId = -1;
    }

    public void addTeleport(JsonArray teleInfo) {
        int id = teleInfo.get(0).getAsInt();
        resetConstants(id);
        targetId = teleInfo.get(3).getAsInt();
        this.setContentLevel("teleport");
    }

    public void addFishInfo(int id, int direction, int color, int queen, int team) {
        this.ids[0] = id;
        this.direction = direction;
        this.color = color;
        this.queen = queen;
        this.team = team;
        this.sick = 0;
        this.targetId = -1;
        this.setContentLevel("fish");
    }

    public void move(Tile tile) {
        tile.setIds(this.ids);
        tile.setDirection(this.direction);
        tile.setColor(this.color);
        tile.setQueen(this.queen);
        tile.setTeam(this.team);
        tile.setSick(this.sick);
        tile.setTargetId(this.targetId);
        tile.setContentLevel(this.contentLevel);
        if (tile != this)
        {
            resetConstants(-1);
        }
    }

    public void clear() {
        resetConstants(-1);
    }

    public void cleanNet() {
        resetConstantsNet(-1);
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