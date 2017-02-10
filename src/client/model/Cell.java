package client.model;

import com.google.gson.JsonArray;

public class Cell {
    private int x;
    private int y;
    private Information cockroachInformation;
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
        if (information instanceof CockroachInformation) {
            cockroachInformation = information;
        } else if (information instanceof ItemInformation) {
            itemInformation = information;
        } else if (information instanceof NetInformation) {
            netInformation = information;
        } else if (information instanceof TeleportInformation) {
            teleportInformation = information;
        }
    }

    public void addCockroachInfo(JsonArray cockroachInfo) {
        cockroachInformation = new CockroachInformation();
        cockroachInformation.setId(cockroachInfo.get(0).getAsInt());
        CockroachInformation chosenCockroachInfo = (CockroachInformation) cockroachInformation;
        chosenCockroachInfo.setDirection(cockroachInfo.get(3).getAsInt());
        chosenCockroachInfo.setColor(cockroachInfo.get(4).getAsInt());
        chosenCockroachInfo.setQueen(cockroachInfo.get(5).getAsInt());
        chosenCockroachInfo.setSick(cockroachInfo.get(6).getAsInt());
        chosenCockroachInfo.setTeam(cockroachInfo.get(7).getAsInt());
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

    public void addCockroachInfo(int id, int direction, int color, int queen, int team) {
        cockroachInformation = new CockroachInformation();
        cockroachInformation.setId(id);
        CockroachInformation chosenCockroachInfo = (CockroachInformation) cockroachInformation;
        chosenCockroachInfo.setDirection(direction);
        chosenCockroachInfo.setColor(color);
        chosenCockroachInfo.setQueen(queen);
        chosenCockroachInfo.setSick(0);
        chosenCockroachInfo.setTeam(team);
    }

    public void clear() {
        cockroachInformation = null;
        itemInformation = null;
    }

    public void cleanNet() {
        netInformation = null;
    }

    public Information getCockroachInformation() {
        return cockroachInformation;
    }

    public void setCockroachInformation(Information cockroachInformation) {
        this.cockroachInformation = cockroachInformation;
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