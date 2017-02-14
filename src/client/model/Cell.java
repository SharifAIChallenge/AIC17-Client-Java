package client.model;

import com.google.gson.JsonArray;

public class Cell {
    private int x;
    private int y;
    private Entity beetleEntity;
    private Entity slipperEntity;
    private Entity teleportEntity;
    private Entity foodEntity;
    private Entity trashEntity;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }


    void addTrash(int id, int trashValidTime) {
        trashEntity = new Trash(id, trashValidTime);
    }


    void addFood(int id, int foodValidTime) {
        foodEntity = new Food(id, foodValidTime);
    }


    public void addSlipper(int id, int slipperValidTime) {
        slipperEntity = new Slipper(id, slipperValidTime);
    }

    public void addTeleport(int id, int targetId) {
        teleportEntity = new Teleport(id, targetId);
    }

    public void receiveInfo(Entity entity) {
        if (entity instanceof Beetle) {
            beetleEntity = entity;
        } else if (entity instanceof Food) {
            foodEntity = entity;
        } else if (entity instanceof Trash) {
            trashEntity = entity;
        } else if (entity instanceof Slipper) {
            slipperEntity = entity;
        } else if (entity instanceof Teleport) {
            teleportEntity = entity;
        }
    }

    public void addBeetleInfo(JsonArray beetleInfo) {
        beetleEntity = new Beetle(beetleInfo.get(0).getAsInt());
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
        beetleEntity = new Beetle(id);
        Beetle chosenBeetleInfo = (Beetle) beetleEntity;
        chosenBeetleInfo.setDirection(direction);
        chosenBeetleInfo.setColor(color);
        chosenBeetleInfo.setQueen(queen);
        chosenBeetleInfo.setSick(0);
        chosenBeetleInfo.setTeam(team);
    }

    public void clear() {
        beetleEntity = null;
        foodEntity = null;
        trashEntity = null;
    }

    public void cleanSlipper() {
        slipperEntity = null;
    }

    public Entity getBeetleEntity() {
        return beetleEntity;
    }

    public void setBeetleEntity(Entity beetleEntity) {
        this.beetleEntity = beetleEntity;
    }

    public Entity getItemEntity() {
        if (foodEntity != null) {
            return foodEntity;
        } else {
            return trashEntity;
        }
    }

    void setItemEntity(Entity itemEntity) {
        if (itemEntity instanceof Food) {
            this.foodEntity = itemEntity;
        } else {
            this.trashEntity = itemEntity;
        }
    }

    public Entity getSlipperEntity() {
        return slipperEntity;
    }

    public void setSlipperEntity(Entity slipperEntity) {
        this.slipperEntity = slipperEntity;
    }

    public Entity getTeleportEntity() {
        return teleportEntity;
    }

    public void setTeleportEntity(Entity teleportEntity) {
        this.teleportEntity = teleportEntity;
    }

    public Entity getFoodEntity() {
        return foodEntity;
    }

    public void setFoodEntity(Entity foodEntity) {
        this.foodEntity = foodEntity;
    }

    public Entity getTrashEntity() {
        return trashEntity;
    }

    public void setTrashEntity(Entity trashEntity) {
        this.trashEntity = trashEntity;
    }


}