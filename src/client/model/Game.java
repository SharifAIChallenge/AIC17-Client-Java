package client.model;

//import client.World;

import client.World;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.model.Event;
import common.network.data.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Game implements World {
    // Yo!
    private int currentTurn = 0;
    private long totalTime;
    private long startTime;
    private int teamID;
    private int width;
    private int height;
    private int myScore;
    private int oppScore;
    private Constants constants;



    private Cell[][] items = new Cell[4][]; // Teleport-0, slipper-1, Trash-2 and food-3 Cells
    private Cell[][] beetles = new Cell[2][]; // my Beetle - 0, opp Beetle - 1

    private HashMap<Integer, Cell> idMap = new HashMap<>();
    private HashMap<Integer, Entity> infoMap = new HashMap<>(); // This field is not usefull for user

    private Consumer<Message> sender;
    private Map map;

    public Game(Consumer<Message> sender) {
        this.sender = sender;
    }

    public void changeStrategy(int color, int right, int front, int left, int strategy) {
        Event event = new Event("s", new Object[]{color, right, front, left, strategy});
        sender.accept(new Message(Event.EVENT, event));
    }

    public void deterministicMove(int id, int s) {
        Event event = new Event("m", new Object[]{id, s});
        sender.accept(new Message(Event.EVENT, event));
    }

    public void antennaChange(int id, int c) {
        Event event = new Event("c", new Object[]{id, c});
        sender.accept(new Message(Event.EVENT, event));
    }

    public void handleInitMessage(Message msg) {
        JsonArray constants = msg.args.get(7).getAsJsonArray();
        this.setConstants(constants);

        teamID = msg.args.get(0).getAsInt();

        JsonArray size = msg.args.get(1).getAsJsonArray();
        width = size.get(0).getAsInt();
        height = size.get(1).getAsInt();

        Cell[][] cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        map = new Map(cells);


        JsonArray Beetles = msg.args.get(2).getAsJsonArray();
        this.beetles = new Cell[2][Beetles.size()];
        int myBeetle = 0;
        int oppBeetle = 0;
        for (int i = 0; i < Beetles.size(); i++) {
            int cellX, cellY;
            JsonArray beetleInfo = Beetles.get(i).getAsJsonArray();
            int id = beetleInfo.get(0).getAsInt();
            cellX = beetleInfo.get(1).getAsInt();
            cellY = beetleInfo.get(2).getAsInt();

            Cell theChosenCell = map.getCell(cellX, cellY);
            theChosenCell.addBeetleInfo(beetleInfo);

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getBeetleEntity());

            if (teamID == beetleInfo.get(7).getAsInt()) {
                this.beetles[0][myBeetle++] = theChosenCell;
            } else {
                this.beetles[1][oppBeetle++] = theChosenCell;
            }
        }

        JsonArray foods = msg.args.get(3).getAsJsonArray();
        Cell[] foodCells = new Cell[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            JsonArray foodInfo = foods.get(i).getAsJsonArray();
            int id = foodInfo.get(0).getAsInt();
            int cellX = foodInfo.get(1).getAsInt();
            int cellY = foodInfo.get(2).getAsInt();

            Cell theChosenCell = map.getCell(cellX, cellY);
            theChosenCell.addFood(id, this.constants.getFoodValidTime());
            foodCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getItemEntity());
        }
        items[3] = foodCells;

        JsonArray trashes = msg.args.get(4).getAsJsonArray();
        Cell[] trashCells = new Cell[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            JsonArray trashInfo = trashes.get(i).getAsJsonArray();
            int id = trashInfo.get(0).getAsInt();
            int cellX = trashInfo.get(1).getAsInt();
            int cellY = trashInfo.get(2).getAsInt();

            Cell theChosenCell = cells[cellX][cellY];
            theChosenCell.addTrash(id, this.constants.getTrashValidTime());
            trashCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getItemEntity());
        }
        items[2] = trashCells;

        JsonArray slippers = msg.args.get(5).getAsJsonArray();
        Cell[] slipperCells = new Cell[slippers.size()];
        for (int i = 0; i < slippers.size(); i++) {
            JsonArray slipperInfo = slippers.get(i).getAsJsonArray();
            int id = slipperInfo.get(0).getAsInt();
            int cellX = slipperInfo.get(1).getAsInt();
            int cellY = slipperInfo.get(2).getAsInt();

            Cell theChosenCell = cells[cellX][cellY];
            theChosenCell.addSlipper(id, this.constants.getNetValidTime());
            slipperCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getSlipperEntity());
        }
        items[1] = slipperCells;

        JsonArray teleports = msg.args.get(6).getAsJsonArray();
        Cell[] teleportCells = new Cell[teleports.size()];
        for (int i = 0; i < teleports.size(); i++) {
            JsonArray teleportInfo = teleports.get(i).getAsJsonArray();
            int id = teleportInfo.get(0).getAsInt();
            int cellX = teleportInfo.get(1).getAsInt();
            int cellY = teleportInfo.get(2).getAsInt();

            Cell theChosenCell = cells[cellX][cellY];
            theChosenCell.addTeleport(id, teleportInfo.get(3).getAsInt());
            teleportCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getTeleportEntity());
        }
        items[0] = teleportCells;



    }

    public void handleTurnMessage(Message msg) {
        currentTurn = msg.args.get(0).getAsInt();

        JsonArray scores = msg.args.get(1).getAsJsonArray();
        myScore = scores.get(teamID).getAsInt();
        oppScore = scores.get(1 - teamID).getAsInt();

        JsonArray allChanges = msg.args.get(2).getAsJsonArray();
        for (int i = 0; i < allChanges.size(); i++) {
            Gson gson = new Gson();
            JsonObject changes = allChanges.get(i).getAsJsonObject();
            String jsonString = changes.toString();
            Change change = gson.fromJson(jsonString, Change.class);
            if (currentTurn == 1) {
                System.out.println("Lol");
            } else if (currentTurn == 2) {
                System.out.println();
            }

            char type = change.getType();
            if (type == 'a') { // add
                ArrayList<ArrayList<Integer>> allAdds = change.getArgs();
                for (int j = 0; j < allAdds.size(); j++) {
                    ArrayList<Integer> addChange = allAdds.get(j);
                    switch (addChange.get(1)) {
                        case 0:
                            addBeetle(addChange);
                            break;
                        case 1:
                            addFood(addChange);
                            break;
                        case 2:
                            addTrash(addChange);
                            break;
                        case 3:
                            addSlipper(addChange);
                            break;
                    }
                }
            } else if (type == 'd') { // delete
                ArrayList<ArrayList<Integer>> allDeletes = change.getArgs();
                for (int j = 0; j < allDeletes.size(); j++) {
                    ArrayList<Integer> deleteChange = allDeletes.get(j);
                    delete(deleteChange);
                }
            } else if (type == 'm') { // moveContent
                ArrayList<ArrayList<Integer>> allMoves = change.getArgs();
                for (int j = 0; j < allMoves.size(); j++) {
                    ArrayList<Integer> moveChange = allMoves.get(j);
                    moveBeetle(moveChange);
                }
            } else if (type == 'c') { // change condition
                ArrayList<ArrayList<Integer>> allAlters = change.getArgs();
                for (int j = 0; j < allAlters.size(); j++) {
                    ArrayList<Integer> alter = allAlters.get(j);
                    if (alter.size() == 5) {
                        beetleAlter(alter);
                    } else {
                        itemAlter(alter);
                    }
                }
            }
        }
		handleFinalChanges();
    }

	private void handleFinalChanges()
	{
		map.setIdMap(idMap);
		handleTeleports();
		handleEntityCells();
		handleFoodRemainings();
		handleTrashRemainings();
		handleSlipperRemainings();
	}
	
	private void handleTeleports()
	{
		Cell[] teleportCells = map.getTeleportCells();
		for(Cell cell : teleportCells)
		{
			Teleport theChosenTeleport = (Teleport) cell.getTeleport();
			int targetId = theChosenTeleport.getTargetId();
			Teleport targetTeleport = (Teleport) idMap.get(targetId).getTeleport();
			theChosenTeleport.setPair(targetTeleport);
		}
	}
	
	private void handleEntityCells()
	{
		for(Integer id : idMap.keySet())
		{
			Entity theChosenEntity = map.getEntity(id);
			theChosenEntity.setCell(idMap.get(id));
		}
	}
	
	private void handleFoodRemainings()
	{
		Cell[] foodCells = map.getFoodCells();
		for(Cell cell : foodCells)
		{
			Food food = (Food) cell.getFood();
			food.setRemainingTurns(food.getRemainingTurns() - 1);
		}
	}
	
	private void handleTrashRemainings()
	{
		Cell[] trashCells = map.getTrashCells();
		for(Cell cell : trashCells)
		{
			Trash trash = (Trash) cell.getTrash();
			trash.setRemainingTurns(trash.getRemainingTurns() - 1);
		}
	}
	
	private void handleSlipperRemainings()
	{
		Cell[] slipperCells = map.getSlipperCells();
		for(Cell cell : slipperCells)
		{
			Slipper slipper = (Slipper) cell.getSlipper();
			slipper.setRemainingTurns(slipper.getRemainingTurns() - 1);
		}
	}
	
    private void itemAlter(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int x = changes.get(1);
        int y = changes.get(2);

        Cell theChosenCell = idMap.get(id);
        Cell[][] cells = map.getCells();
        Cell targetCell = cells[x][y];

        targetCell.receiveInfo(theChosenCell.getItemEntity());
        idMap.put(id, targetCell);
        theChosenCell.setItemEntity(null);
		
		if(map.getEntityType(id) == EntityType.Food)
		{
			Cell[] foods = map.getFoodCells();
			ArrayList<Cell> foodList = new ArrayList<Cell>(Arrays.asList(foods));
			foodList.remove(theChosenCell);
			foodList.add(targetCell);
			Cell[] tempCell = new Cell[foodList.size()];
			foods = foodList.toArray(tempCell);
			map.setFoodCells(foods);
		}
		else
		{
			Cell[] trashes = map.getTrashCells();
			ArrayList<Cell> trashList = new ArrayList<Cell>(Arrays.asList(trashes));
			trashList.remove(theChosenCell);
			trashList.add(targetCell);
			Cell[] tempCell = new Cell[trashList.size()];
			trashes = trashList.toArray(tempCell);
			map.setTrashCells(trashes);
		}
    }

    private void delete(ArrayList<Integer> changes) {
        int id = changes.get(0);

        Cell theChosenCell = idMap.get(id);
        idMap.remove(id);

        Entity theChosenInfo = infoMap.get(id);
        infoMap.remove(id);

        if (theChosenInfo instanceof Beetle) {
            Beetle chosenBeetleInfo = (Beetle) theChosenInfo;
            if (chosenBeetleInfo.getTeam() == teamID) {
				Cell[] beetles = map.getMyCells();
                ArrayList<Cell> beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
                beetleList.remove(theChosenCell);
                Cell[] tempCell = new Cell[beetleList.size()];
                beetles = beetleList.toArray(tempCell);
				map.setMyCells(beetles);
            } else {
				Cell[] beetles = map.getOppCells();
                ArrayList<Cell> beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
                beetleList.remove(theChosenCell);
                Cell[] tempCell = new Cell[beetleList.size()];
                beetles = beetleList.toArray(tempCell);
				map.setOppCells(beetles);
            }
            theChosenCell.clear();
        } else if (theChosenInfo instanceof Food) {
			Cell[] foods = map.getFoodCells();
            ArrayList<Cell> foodList = new ArrayList<Cell>(Arrays.asList(foods));
            foodList.remove(theChosenCell);
            Cell[] tempCell = new Cell[foodList.size()];
            foods = foodList.toArray(tempCell);
			map.setFoodCells(foods);
            theChosenCell.clear();
        } else if (theChosenInfo instanceof Trash) {
			Cell[] trashes = map.getTrashCells();
            ArrayList<Cell> trashList = new ArrayList<Cell>(Arrays.asList(trashes));
            trashList.remove(theChosenCell);
            Cell[] tempCell = new Cell[trashList.size()];
            trashes = trashList.toArray(tempCell);
			map.setTrashCells(trashes);
            theChosenCell.clear();
        } else if (theChosenInfo instanceof Slipper) {
			Cell[] slippers = map.getSlipperCells();
            ArrayList<Cell> slipperList = new ArrayList<Cell>(Arrays.asList(slippers));
            slipperList.remove(theChosenCell);
            Cell[] tempCell = new Cell[slipperList.size()];
            slippers = slipperList.toArray(tempCell);
            map.setSlipperCells(slipper);
			theChosenCell.cleanSlipper();
        }
    }

    private void moveBeetle(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int move = changes.get(1);
        Cell theChosenCell = idMap.get(id);
        Beetle theChosenInfo = (Beetle) (infoMap.get(id));
        switch (move) {
            case 0:
                theChosenInfo.setDirection((theChosenInfo.getDirectionInt() + 3) % 4);
                break;
            case 1:
                int cellX = nextX(theChosenCell, theChosenInfo.getDirectionInt());
                int cellY = nextY(theChosenCell, theChosenInfo.getDirectionInt());
                Cell targetCell = map.getCell(cellX, cellY);
                targetCell.receiveInfo(theChosenInfo);
                idMap.put(id, targetCell);
                System.out.println("-----------------------");
                System.out.println(id);
                System.out.println(targetCell.getBeetleEntity());
                System.out.println(theChosenCell.getBeetleEntity());
                System.out.println("-----------------------");
                if (targetCell.getBeetleEntity().getId() == theChosenCell.getBeetleEntity().getId()) {
                    theChosenCell.clear();
                }
                break;
            case 2:
                theChosenInfo.setDirection((theChosenInfo.getDirectionInt() + 1) % 4);
                break;
        }
    }

    private int nextX(Cell cell, int dir) {
        int direction = dir;
        int x = cell.getX();
        switch (direction) {
            case 3:
                x = (x + 1) % 8;
                break;
            case 1:
                x = (x + 7) % 8;
                break;
        }
        return x;
    }

    private int nextY(Cell cell, int dir) {
        int direction = dir;
        int y = cell.getY();
        switch (direction) {
            case 2:
                y = (y + 7) % 8;
                break;
            case 0:
                y = (y + 1) % 8;
                break;
        }
        return y;
    }

    private void beetleAlter(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int newX = changes.get(1);
        int newY = changes.get(2);
        int color = changes.get(3);
        int sick = changes.get(4);
        
		Cell theChosenCell = idMap.get(id);
        Beetle theChosenInfo = (Beetle) infoMap.get(id);
        Cell targetCell = map.getCell(newX, newY);
        theChosenInfo.setColor(color);
        theChosenInfo.setSick(sick);
        
		targetCell.receiveInfo(theChosenInfo);
        idMap.put(id, targetCell);
        if (targetCell != theChosenCell) {
            theChosenCell.clear();
			
			int team =((Beetle) (map.getEntity(id))).getTeam();
			if (team == teamID) {
				Cell[] beetles = map.getMyCells();
				beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
				beetleList.remove(theChosenCell);
				beetleList.add(targetCell);
				Cell[] tempCell = new Cell[beetleList.size()];
				beetles = beetleList.toArray(tempCell);
				map.setMyCells(beetles);
			} else {
				Cell[] beetles = map.getOppCells();
				beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
				beetleList.remove(theChosenCell);
				beetleList.add(targetCell);
				Cell[] tempCell = new Cell[beetleList.size()];
				beetles = beetleList.toArray(tempCell);
				map.setOppCells(beetles);
			}
        }
    }

    private void setConstants(JsonArray constants) {
        this.constants = new Constants();
        this.constants.setTurnTimeout((int) constants.get(0).getAsDouble());
        this.constants.setFoodProb(constants.get(1).getAsDouble());
        this.constants.setTrashProb(constants.get(2).getAsDouble());
        this.constants.setNetProb(constants.get(3).getAsDouble());
        this.constants.setNetValidTime((int) constants.get(4).getAsDouble());
        this.constants.setColorCost((int) constants.get(5).getAsDouble());
        this.constants.setSickCost((int) constants.get(6).getAsDouble());
        this.constants.setUpdateCost((int) constants.get(7).getAsDouble());
        this.constants.setDetMoveCost((int) constants.get(8).getAsDouble());
        this.constants.setKillQueenScore((int) constants.get(9).getAsDouble());
        this.constants.setKillBothQueenScore((int) constants.get(10).getAsDouble());
        this.constants.setKillFishScore((int) constants.get(11).getAsDouble());
        this.constants.setQueenCollisionScore((int) constants.get(12).getAsDouble());
        this.constants.setFishFoodScore((int) constants.get(13).getAsDouble());
        this.constants.setQueenFoodScore((int) constants.get(14).getAsDouble());
        this.constants.setSickLifeTime((int) constants.get(15).getAsDouble());
        this.constants.setPowerRatio(constants.get(16).getAsDouble());
        this.constants.setEndRatio(constants.get(17).getAsDouble());
        this.constants.setDisobeyNum((int) constants.get(18).getAsDouble());
        this.constants.setFoodValidTime((int) constants.get(19).getAsDouble());
        this.constants.setTrashValidTime((int) constants.get(20).getAsDouble());
    }

    private void addBeetle(ArrayList<Integer> changes) {
        ArrayList<Cell> beetleList;
        int id = changes.get(0);
        int cellX = changes.get(2);
        int cellY = changes.get(3);
        int direction = changes.get(4);
        int color = changes.get(5);
        int queen = changes.get(6);
        int team = changes.get(7);

        Cell theChosenCell = map.getCell(cellX, cellY);
        theChosenCell.addBeetleInfo(id, direction, color, queen, team);

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getBeetleEntity());

        if (team == teamID) {
			Cell[] beetles = map.getMyCells();
            beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
            beetleList.add(theChosenCell);
            Cell[] tempCell = new Cell[beetleList.size()];
            beetles = beetleList.toArray(tempCell);
			map.setMyCells(beetles);
		} else {
			Cell[] beetles = map.getOppCells();
            beetleList = new ArrayList<Cell>(Arrays.asList(beetles));
            beetleList.add(theChosenCell);
            Cell[] tempCell = new Cell[beetleList.size()];
            beetles = beetleList.toArray(tempCell);
			map.setOppCells(beetles);
        }
    }

    private void addFood(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int cellX = changes.get(2);
        int cellY = changes.get(3);

        Cell theChosenCell = map.getCell(cellX, cellY);
        theChosenCell.addFood(id, constants.getFoodValidTime());

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getItemEntity());

		Cell[] foods = map.getFoodCells();
        ArrayList<Cell> foodList = new ArrayList<Cell>(Arrays.asList(foods));
        foodList.add(theChosenCell);
        Cell[] tempCell = new Cell[foodList.size()];
        foods = foodList.toArray(tempCell);
		map.setFoodCells(foods);
    }

    private void addTrash(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int cellX = changes.get(2);
        int cellY = changes.get(3);

        Cell theChosenCell = map.getCell(cellX, cellY);
        theChosenCell.addTrash(id, constants.getTrashValidTime());

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getItemEntity());

		Cell[] trashes = map.getTrashCells();
        ArrayList<Cell> trashList = new ArrayList<Cell>(Arrays.asList(trashes));
        trashList.add(theChosenCell);
        Cell[] tempCell = new Cell[trashList.size()];
        trashes = trashList.toArray(tempCell);
		map.setTrashCells(trashes);
    }

    private void addSlipper(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int cellX = changes.get(2);
        int cellY = changes.get(3);

        Cell theChosenCell = map.getCell(cellX, cellY);
        theChosenCell.addSlipper(id, constants.getNetValidTime());

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getSlipperEntity());

		Cell[] slippers = map.getSlipperCells();
        ArrayList<Cell> slipperList = new ArrayList<Cell>(Arrays.asList(slippers));
        slipperList.add(theChosenCell);
        Cell[] tempCell = new Cell[slipperList.size()];
        slippers = slipperList.toArray(tempCell);
		map.setSlipperCells(slippers);
    }

    public Map getMap() {
        return map;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public long getTotalTime() {
        return System.currentTimeMillis() - startTime;
    }


    public int getTeamID() {
        return teamID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMyScore() {
        return myScore;
    }

    public int getOppScore() {
        return oppScore;
    }

    @Override
    public int getTotalTurns() {
        return 0;
    }

    @Override
    public long getTurnRemainingTime() {
        return 0;
    }

    @Override
    public long getTurnTotalTime() {
        return 0;
    }

    @Override
    public Constants getConstants() {
        return this.constants;
    }
}