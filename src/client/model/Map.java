package client.model;

import java.util.HashMap;

public class Map {
    private Cell[][] cells;
    private int height;
    private int width;

    private Cell[][] items = new Cell[4][]; // Teleport-0, net-1, Trash-2 and food-3 Cells
    private Cell[][] beetles = new Cell[2][]; // my Beetle - 0, opp Beetle - 1

    private HashMap<Integer, Cell> idMap = new HashMap<>();

    public Map(Cell[][] cells, int width, int height) {
        this.cells = cells;
        this.width = width;
        this.height = height;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Cell[] getMyCells() {
        return beetles[0];
    }

    void setMyCells(Cell[] myCells) {
        this.beetles[0] = myCells;
    }

    public Cell[] getOppCells() {
        return beetles[1];
    }

    void setOppCells(Cell[] oppCells) {
        this.beetles[1] = oppCells;
    }

    public Cell[] getTeleportCells() {
        return items[0];
    }

    void setTeleportCells(Cell[] teleportCells) {
        this.items[0] = teleportCells;
    }

    public Cell[] getSlipperCells() {
        return items[1];
    }

    void setSlipperCells(Cell[] netCells) {
        this.items[1] = netCells;
    }

    public Cell[] getTrashCells() {
        return items[2];
    }

    void setTrashCells(Cell[] trashCells) {
        this.items[2] = trashCells;
    }

    public Cell[] getFoodCells() {
        return items[3];
    }

    void setFoodCells(Cell[] foodCells) {
        this.items[3] = foodCells;
    }

    void setIdMap(HashMap<Integer, Cell> idMap) {
        this.idMap = idMap;
    }

    public Entity getEntity(int id) {
        Cell theChosenCell = idMap.get(id);

        if (theChosenCell.getBeetleEntity() != null && theChosenCell.getBeetleEntity().getId() == id) {
            return theChosenCell.getBeetleEntity();
        } else if (theChosenCell.getFoodEntity() != null && theChosenCell.getFoodEntity().getId() == id) {
            return theChosenCell.getFoodEntity();
        } else if (theChosenCell.getTrashEntity() != null && theChosenCell.getTrashEntity().getId() == id) {
            return theChosenCell.getTrashEntity();
        } else if (theChosenCell.getSlipperEntity() != null && theChosenCell.getSlipperEntity().getId() == id) {
            return theChosenCell.getSlipperEntity();
        } else if (theChosenCell.getTeleportEntity() != null && theChosenCell.getTeleportEntity().getId() == id) {
            return theChosenCell.getTeleportEntity();
        }
        return null;
    }

    public EntityType getEntityType(int id) {
        return getEntity(id).getType();
    }

    void setBeetles(Cell[][] beetles) {
        this.beetles = beetles;
    }

    public void setItems(Cell[][] items) {
        this.items = items;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    void setHeight(int height) {
        this.height = height;
    }

    void setWidth(int width) {
        this.width = width;
    }
}