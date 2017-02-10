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

    // Constants
    private double foodProb;
    private double trashProb;
    private double netProb;
    private int netValidTime;
    private int turnTimeout;
    private int colorCost;
    private int sickCost;
    private int updateCost;
    private int detMoveCost;
    private int killQueenScore;
    private int killBothQueenScore;
    private int killFishScore;
    private int queenCollisionScore;
    private int fishFoodScore;
    private int queenFoodScore;
    private int sickLifeTime;
    private double powerRatio;
    private double endRatio;
    private int disobeyNum;
    private int foodValidTime;
    private int trashValidTime;

    private Cell[][] items = new Cell[4][]; // Teleport-0, net-1, Trash-2 and food-3 tiles
    private Cell[][] beetles = new Cell[2][]; // my Beetle - 0, opp Beetle - 1

    private HashMap<Integer, Cell> idMap = new HashMap<>();
    private HashMap<Integer, Information> infoMap = new HashMap<>(); // This field is not usefull for user

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
            int tileX, tileY;
            JsonArray beetleInfo = Beetles.get(i).getAsJsonArray();
            int id = beetleInfo.get(0).getAsInt();
            tileX = beetleInfo.get(1).getAsInt();
            tileY = beetleInfo.get(2).getAsInt();

            Cell theChosenCell = map.getTile(tileX, tileY);
            theChosenCell.addBeetleInfo(beetleInfo);

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getBeetleInformation());

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
            int tileX = foodInfo.get(1).getAsInt();
            int tileY = foodInfo.get(2).getAsInt();

            Cell theChosenCell = map.getTile(tileX, tileY);
            theChosenCell.addItem(id, 0);
            foodCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getItemInformation());
        }
        items[3] = foodCells;

        JsonArray trashes = msg.args.get(4).getAsJsonArray();
        Cell[] trashCells = new Cell[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            JsonArray trashInfo = trashes.get(i).getAsJsonArray();
            int id = trashInfo.get(0).getAsInt();
            int tileX = trashInfo.get(1).getAsInt();
            int tileY = trashInfo.get(2).getAsInt();

            Cell theChosenCell = cells[tileX][tileY];
            theChosenCell.addItem(id, 1);
            trashCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getItemInformation());
        }
        items[2] = trashCells;

        JsonArray nets = msg.args.get(5).getAsJsonArray();
        Cell[] netCells = new Cell[nets.size()];
        for (int i = 0; i < nets.size(); i++) {
            JsonArray netInfo = nets.get(i).getAsJsonArray();
            int id = netInfo.get(0).getAsInt();
            int tileX = netInfo.get(1).getAsInt();
            int tileY = netInfo.get(2).getAsInt();

            Cell theChosenCell = cells[tileX][tileY];
            theChosenCell.addNet(id);
            netCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getNetInformation());
        }
        items[1] = netCells;

        JsonArray teleports = msg.args.get(6).getAsJsonArray();
        Cell[] teleportCells = new Cell[teleports.size()];
        for (int i = 0; i < teleports.size(); i++) {
            JsonArray teleportInfo = teleports.get(i).getAsJsonArray();
            int id = teleportInfo.get(0).getAsInt();
            int tileX = teleportInfo.get(1).getAsInt();
            int tileY = teleportInfo.get(2).getAsInt();

            Cell theChosenCell = cells[tileX][tileY];
            theChosenCell.addTeleport(id, teleportInfo.get(3).getAsInt());
            teleportCells[i] = theChosenCell;

            idMap.put(id, theChosenCell);
            infoMap.put(id, theChosenCell.getTeleportInformation());
        }
        items[0] = teleportCells;

        JsonArray constants = msg.args.get(7).getAsJsonArray();
        this.setConstants(constants);

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
                            addNet(addChange);
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
    }

    private void itemAlter(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int x = changes.get(1);
        int y = changes.get(2);

        Cell theChosenCell = idMap.get(id);
        Cell[][] cells = map.getCells();
        Cell targetCell = cells[x][y];

        targetCell.receiveInfo(theChosenCell.getItemInformation());
        idMap.put(id, targetCell);
        theChosenCell.setItemInformation(null);
    }

    private void delete(ArrayList<Integer> changes) {
        int id = changes.get(0);

        Cell theChosenCell = idMap.get(id);
        idMap.remove(id);

        Information theChosenInfo = infoMap.get(id);
        infoMap.remove(id);

        if (theChosenInfo instanceof BeetleInformation) {
            BeetleInformation chosenBeetleInfo = (BeetleInformation) theChosenInfo;
            if (chosenBeetleInfo.getTeam() == teamID) {
                ArrayList<Cell> beetleList = new ArrayList<Cell>(Arrays.asList(beetles[0]));
                beetleList.remove(theChosenCell);
                Cell[] tempCell = new Cell[beetleList.size()];
                beetles[0] = beetleList.toArray(tempCell);
            } else {
                ArrayList<Cell> beetleList = new ArrayList<Cell>(Arrays.asList(beetles[1]));
                beetleList.remove(theChosenCell);
                Cell[] tempCell = new Cell[beetleList.size()];
                beetles[1] = beetleList.toArray(tempCell);
            }
            theChosenCell.clear();
        } else if (theChosenInfo instanceof ItemInformation && ((ItemInformation) theChosenInfo).getItemId() == 0) {
            ArrayList<Cell> foodList = new ArrayList<Cell>(Arrays.asList(items[3]));
            foodList.remove(theChosenCell);
            Cell[] tempCell = new Cell[foodList.size()];
            items[3] = foodList.toArray(tempCell);
            theChosenCell.clear();
        } else if (theChosenInfo instanceof ItemInformation && ((ItemInformation) theChosenInfo).getItemId() == 1) {
            ArrayList<Cell> trashList = new ArrayList<Cell>(Arrays.asList(items[2]));
            trashList.remove(theChosenCell);
            Cell[] tempCell = new Cell[trashList.size()];
            items[2] = trashList.toArray(tempCell);
            theChosenCell.clear();
        } else if (theChosenInfo instanceof NetInformation) {
            ArrayList<Cell> netList = new ArrayList<Cell>(Arrays.asList(items[1]));
            netList.remove(theChosenCell);
            Cell[] tempCell = new Cell[netList.size()];
            items[1] = netList.toArray(tempCell);
            theChosenCell.cleanNet();
        }
    }

    private void moveBeetle(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int move = changes.get(1);
        Cell theChosenCell = idMap.get(id);
        BeetleInformation theChosenInfo = (BeetleInformation) (infoMap.get(id));
        switch (move) {
            case 0:
                theChosenInfo.setDirection((theChosenInfo.getDirection() + 3) % 4);
                break;
            case 1:
                int tileX = nextX(theChosenCell, theChosenInfo.getDirection());
                int tileY = nextY(theChosenCell, theChosenInfo.getDirection());
                Cell targetCell = map.getTile(tileX, tileY);
                targetCell.receiveInfo(theChosenInfo);
                idMap.put(id, targetCell);
                System.out.println("-----------------------");
                System.out.println(id);
                System.out.println(targetCell.getBeetleInformation());
                System.out.println(theChosenCell.getBeetleInformation());
                System.out.println("-----------------------");
                if (targetCell.getBeetleInformation().getId() == theChosenCell.getBeetleInformation().getId()) {
                    theChosenCell.clear();
                }
                break;
            case 2:
                theChosenInfo.setDirection((theChosenInfo.getDirection() + 1) % 4);
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
        BeetleInformation theChosenInfo = (BeetleInformation) infoMap.get(id);
        Cell targetCell = map.getTile(newX, newY);
        theChosenInfo.setColor(color);
        theChosenInfo.setSick(sick);
        targetCell.receiveInfo(theChosenInfo);
        idMap.put(id, targetCell);
        if (targetCell != theChosenCell) {
            theChosenCell.clear();
        }
    }

    private void setConstants(JsonArray constants) {

        turnTimeout = (int) constants.get(0).getAsDouble();
        foodProb = constants.get(1).getAsDouble();
        trashProb = constants.get(2).getAsDouble();
        netProb = constants.get(3).getAsDouble();
        netValidTime = (int) constants.get(4).getAsDouble();
        colorCost = (int) constants.get(5).getAsDouble();
        sickCost = (int) constants.get(6).getAsDouble();
        updateCost = (int) constants.get(7).getAsDouble();
        detMoveCost = (int) constants.get(8).getAsDouble();
        killQueenScore = (int) constants.get(9).getAsDouble();
        killBothQueenScore = (int) constants.get(10).getAsDouble();
        killFishScore = (int) constants.get(11).getAsDouble();
        queenCollisionScore = (int) constants.get(12).getAsDouble();
        fishFoodScore = (int) constants.get(13).getAsDouble();
        queenFoodScore = (int) constants.get(14).getAsDouble();
        sickLifeTime = (int) constants.get(15).getAsDouble();
        powerRatio = constants.get(16).getAsDouble();
        endRatio = constants.get(17).getAsDouble();
        disobeyNum = (int) constants.get(18).getAsDouble();
        foodValidTime = (int) constants.get(19).getAsDouble();
        trashValidTime = (int) constants.get(20).getAsDouble();
    }

    private void addBeetle(ArrayList<Integer> changes) {
        ArrayList<Cell> beetleList;
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);
        int direction = changes.get(4);
        int color = changes.get(5);
        int queen = changes.get(6);
        int team = changes.get(7);

        Cell theChosenCell = map.getTile(tileX, tileY);
        theChosenCell.addBeetleInfo(id, direction, color, queen, team);

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getBeetleInformation());

        if (team == teamID) {
            beetleList = new ArrayList<Cell>(Arrays.asList(beetles[0]));
            beetleList.add(theChosenCell);
            Cell[] tempCell = new Cell[beetleList.size()];
            beetles[0] = beetleList.toArray(tempCell);
        } else {
            beetleList = new ArrayList<Cell>(Arrays.asList(beetles[1]));
            beetleList.add(theChosenCell);
            Cell[] tempCell = new Cell[beetleList.size()];
            beetles[1] = beetleList.toArray(tempCell);
        }
    }

    private void addFood(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Cell theChosenCell = map.getTile(tileX, tileY);
        theChosenCell.addItem(id, 0);

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getItemInformation());

        ArrayList<Cell> foodList = new ArrayList<Cell>(Arrays.asList(items[3]));
        foodList.add(theChosenCell);
        Cell[] tempCell = new Cell[foodList.size()];
        items[3] = foodList.toArray(tempCell);
    }

    private void addTrash(ArrayList<Integer> changes) {
//        Cell[][] tiles = map.getCells();
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Cell theChosenCell = map.getTile(tileX, tileY);
        theChosenCell.addItem(id, 1);

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getItemInformation());

        ArrayList<Cell> trashList = new ArrayList<Cell>(Arrays.asList(items[2]));
        trashList.add(theChosenCell);
        Cell[] tempCell = new Cell[trashList.size()];
        items[2] = trashList.toArray(tempCell);
    }

    private void addNet(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Cell theChosenCell = map.getTile(tileX, tileY);
        theChosenCell.addNet(id);

        idMap.put(id, theChosenCell);
        infoMap.put(id, theChosenCell.getNetInformation());

        ArrayList<Cell> netList = new ArrayList<Cell>(Arrays.asList(items[1]));
        netList.add(theChosenCell);
        Cell[] tempCell = new Cell[netList.size()];
        items[1] = netList.toArray(tempCell);
    }

    public Cell[] getMyTiles() {
        return beetles[0];
    }

    public Cell[] getOppTiles() {
        return beetles[1];
    }

    public Cell[] getTeleportTiles() {
        return items[0];
    }

    public Cell[] getNetTiles() {
        return items[1];
    }

    public Cell[] getTrashTiles() {
        return items[2];
    }

    public Cell[] getFoodTiles() {
        return items[3];
    }

    public BeetleInformation getBeetleInformation(int id) {
        Cell beetleCell = idMap.get(id);
        BeetleInformation theChosenInfo = (BeetleInformation) beetleCell.getBeetleInformation();
        theChosenInfo.setX(beetleCell.getX());
        theChosenInfo.setY(beetleCell.getY());
        return theChosenInfo;
    }

    public ItemInformation getItemInformation(int id) {
        Cell itemCell = idMap.get(id);
        ItemInformation theChosenInfo = (ItemInformation) itemCell.getItemInformation();
        theChosenInfo.setX(itemCell.getX());
        theChosenInfo.setY(itemCell.getY());
        return theChosenInfo;
    }

    public NetInformation getNetInformation(int id) {
        Cell netCell = idMap.get(id);
        NetInformation theChosenInfo = (NetInformation) netCell.getNetInformation();
        theChosenInfo.setX(netCell.getX());
        theChosenInfo.setY(netCell.getY());
        return theChosenInfo;
    }

    public TeleportInformation getTeleportInformation(int id) {
        Cell teleCell = idMap.get(id);
        TeleportInformation theChosenInfo = (TeleportInformation) teleCell.getTeleportInformation();
        theChosenInfo.setX(teleCell.getX());
        theChosenInfo.setY(teleCell.getY());
        return theChosenInfo;
    }

    public Map getMap() {
        return map;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public double getFoodProb() {
        return foodProb;
    }

    public double getTrashProb() {
        return trashProb;
    }

    public double getNetProb() {
        return netProb;
    }

    public int getTurnTimeout() {
        return turnTimeout;
    }

    public int getColorCost() {
        return colorCost;
    }

    public int getSickCost() {
        return sickCost;
    }

    public int getUpdateCost() {
        return updateCost;
    }

    public int getDetMoveCost() {
        return detMoveCost;
    }

    public int getKillQueenScore() {
        return killQueenScore;
    }

    public int getKillBothQueenScore() {
        return killBothQueenScore;
    }

    public int getKillFishScore() {
        return killFishScore;
    }

    public int getFishFoodScore() {
        return fishFoodScore;
    }

    public int getQueenFoodScore() {
        return queenFoodScore;
    }

    public int getSickLifeTime() {
        return sickLifeTime;
    }

    public double getPowerRatio() {
        return powerRatio;
    }

    public double getEndRatio() {
        return endRatio;
    }

    public int getDisobeyNum() {
        return disobeyNum;
    }

    public int getFoodValidTime() {
        return foodValidTime;
    }

    public int getTrashValidTime() {
        return trashValidTime;
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

    public int getNetValidTime() {
        return netValidTime;
    }
}