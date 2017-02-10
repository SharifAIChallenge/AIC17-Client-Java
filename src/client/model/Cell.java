package client.model;

import com.google.gson.JsonArray;

public class Cell {
    private int x;
    private int y;
    private Information beetleInformation;
    private Information itemInformation;
    private Information netInformation;
    private Information teleportInformation;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addItem(int id, int itemId) {
        itemInformation = new ItemInformation(id, itemId);
    }

    public void addNet(int id) {
        netInformation = new NetInformation(id);
    }

    public void addTeleport(int id, int targetId) {
        teleportInformation = new TeleportInformation(id, targetId);
    }

    public void receiveInfo(Information information) {
        if (information instanceof BeetleInformation) {
            beetleInformation = information;
        } else if (information instanceof ItemInformation) {
            itemInformation = information;
        } else if (information instanceof NetInformation) {
            netInformation = information;
        } else if (information instanceof TeleportInformation) {
            teleportInformation = information;
        }
    }

    public void addBeetleInfo(JsonArray beetleInfo) {
        beetleInformation = new BeetleInformation();
        beetleInformation.setId(beetleInfo.get(0).getAsInt());
        BeetleInformation chosenBeetleInfo = (BeetleInformation) beetleInformation;
        chosenBeetleInfo.setDirection(beetleInfo.get(3).getAsInt());
        chosenBeetleInfo.setColor(beetleInfo.get(4).getAsInt());
        chosenBeetleInfo.setQueen(beetleInfo.get(5).getAsInt());
        chosenBeetleInfo.setSick(beetleInfo.get(6).getAsInt());
        chosenBeetleInfo.setTeam(beetleInfo.get(7).getAsInt());
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

    public void addBeetleInfo(int id, int direction, int color, int queen, int team) {
        beetleInformation = new BeetleInformation();
        beetleInformation.setId(id);
        BeetleInformation chosenBeetleInfo = (BeetleInformation) beetleInformation;
        chosenBeetleInfo.setDirection(direction);
        chosenBeetleInfo.setColor(color);
        chosenBeetleInfo.setQueen(queen);
        chosenBeetleInfo.setSick(0);
        chosenBeetleInfo.setTeam(team);
    }

    public void clear() {
        beetleInformation = null;
        itemInformation = null;
    }

    public void cleanNet() {
        netInformation = null;
    }

    public Information getBeetleInformation() {
        return beetleInformation;
    }

    public void setBeetleInformation(Information beetleInformation) {
        this.beetleInformation = beetleInformation;
    }

    public Information getItemInformation() {
        return itemInformation;
    }

    public void setItemInformation(Information itemInformation) {
        this.itemInformation = itemInformation;
    }

    public Information getNetInformation() {
        return netInformation;
    }

    public void setNetInformation(Information netInformation) {
        this.netInformation = netInformation;
    }

    public Information getTeleportInformation() {
        return teleportInformation;
    }

    public void setTeleportInformation(Information teleportInformation) {
        this.teleportInformation = teleportInformation;
    }
}