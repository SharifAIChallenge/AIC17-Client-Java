package client.model;

import com.google.gson.JsonArray;

public class Cell {
    private int x;
    private int y;
    private Entity beetleEntity;
    private Entity itemEntity;
    private Entity netEntity;
    private Entity teleportEntity;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addItem(int id, int itemId) {
        itemEntity = new ItemEntity(id, itemId);
    }

    public void addNet(int id) {
        netEntity = new Slipper(id);
    }

    public void addTeleport(int id, int targetId) {
        teleportEntity = new Teleport(id, targetId);
    }

    public void receiveInfo(Entity entity) {
        if (entity instanceof Beetle) {
            beetleEntity = entity;
        } else if (entity instanceof ItemEntity) {
            itemEntity = entity;
        } else if (entity instanceof Slipper) {
            netEntity = entity;
        } else if (entity instanceof Teleport) {
            teleportEntity = entity;
        }
    }

    public void addBeetleInfo(JsonArray beetleInfo) {
        beetleEntity = new Beetle();
        beetleEntity.setId(beetleInfo.get(0).getAsInt());
        Beetle chosenBeetleInfo = (Beetle) beetleEntity;
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
        beetleEntity = new Beetle();
        beetleEntity.setId(id);
        Beetle chosenBeetleInfo = (Beetle) beetleEntity;
        chosenBeetleInfo.setDirection(direction);
        chosenBeetleInfo.setColor(color);
        chosenBeetleInfo.setQueen(queen);
        chosenBeetleInfo.setSick(0);
        chosenBeetleInfo.setTeam(team);
    }

    public void clear() {
        beetleEntity = null;
        itemEntity = null;
    }

    public void cleanNet() {
        netEntity = null;
    }

    public Entity getBeetleEntity() {
        return beetleEntity;
    }

    public void setBeetleEntity(Entity beetleEntity) {
        this.beetleEntity = beetleEntity;
    }

    public Entity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(Entity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public Entity getNetEntity() {
        return netEntity;
    }

    public void setNetEntity(Entity netEntity) {
        this.netEntity = netEntity;
    }

    public Entity getTeleportEntity() {
        return teleportEntity;
    }

    public void setTeleportEntity(Entity teleportEntity) {
        this.teleportEntity = teleportEntity;
    }
}