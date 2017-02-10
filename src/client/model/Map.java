package client.model;

public class Map {
    private Cell[][] cells;
	private int height;
	private int width;

	private Cell[][] items = new Cell[4][]; // Teleport-0, net-1, Trash-2 and food-3 Cells
    private Cell[][] beetles = new Cell[2][]; // my Beetle - 0, opp Beetle - 1
	
	private HashMap<Integer, Cell> idMap;
	
    public Map(Cell[][] cells) {
        this.cells = cells;
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
	
	public void setMyCells(Cell[] myCells) {
		this.beetles[0] myCells;
	}

    public Cell[] getOppCells() {
        return beetles[1];
    }
	
	public void setOppCells(Cell[] oppCells) {
		this.beetles[1] = oppCells;
	}

    public Cell[] getTeleportCells() {
        return items[0];
    }
	
	public void setTeleportCells(Cell[] teleportCells) {
		this.items[0] = teleportCells;
	}

    public Cell[] getNetCells() {
        return items[1];
    }
	
	public void setNetCells(Cell[] netCells) {
		this.items[1] = netCells;
	}

    public Cell[] getTrashCells() {
        return items[2];
    }
	
	public void setTrashCells(Cell[] trashCells) {
		this.items[2] = trashCells;
	}

    public Cell[] getFoodCells() {
        return items[3];
    }
	
	public void setFoodCells(Cell[] foodCells) {
		this.items[3] foodCells;
	}
	
	public void setIdMap(HashMap<Integer, Cell> idMap) {
		this.idMap = idMap;
	}
	
	public Entity getEntity(int id) {
		Cell theChosenCell = idMap.get(id);
		
		if (theChosenCell.getBeetleEntity().getId() == id) {
			return theChosenCell.getBeetleEntity();
		} else if(theChosenCell.getFoodEntity().getId() == id)
		{
			return theChosenCell.getFoodEntity();
		} else if(theChosenCell.getTrashEntity().getId() == id)
		{
			return theChosenCell.getTrashEntity();
		}
		else if(slipper = theChosenCell.getSlipperEntity().getId() == id)
		{
			return slipper = theChosenCell.getSlipperEntity();
		}
		else if(theChosenCell.getTeleportEntity().getId() == id)
		{
			return theChosenCell.getTeleportEntity();
		}
	}
	
	public EntityType getEntityType(int id) {
		return idMap.get(id).getType();
	}
	
	public void setBeetles(Cell[][] beetles)
	{
		this.beetles = beetles;
	}
	
	public void setItems(Cell[][] items)
	{
		this.items = items;
	}
}