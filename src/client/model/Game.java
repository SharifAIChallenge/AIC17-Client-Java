package client.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.model.Event;
import common.network.data.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Game {
    // Yo!
    private int totalTurns;
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
    private Tile[][] items = new Tile[4][]; // Teleport-0, net-1, Trash(:D)-2 and food-3 tiles
    private Tile[][] fishes = new Tile[2][]; // my fish - 0, opp fish - 1

    private HashMap<Integer, Tile> idMap = new HashMap<>();
    private HashMap<Integer, Information> infoMap = new HashMap<>(); // Don't use this on your doTurn method :\

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

        Tile[][] tiles = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
        map = new Map(tiles);


        JsonArray fishes = msg.args.get(2).getAsJsonArray();
        this.fishes = new Tile[2][fishes.size()];
        int myfish = 0;
        int opfish = 0;
        for (int i = 0; i < fishes.size(); i++) {
            int tileX, tileY;
            JsonArray fishInfo = fishes.get(i).getAsJsonArray();
            int id = fishInfo.get(0).getAsInt();
            tileX = fishInfo.get(1).getAsInt();
            tileY = fishInfo.get(2).getAsInt();

            Tile theChosenTile = map.getTile(tileX, tileY);
            theChosenTile.addFishInfo(fishInfo);

            idMap.put(id, theChosenTile);
            infoMap.put(id, theChosenTile.getFishInformation());

            if (teamID == fishInfo.get(7).getAsInt()) {
                this.fishes[0][myfish++] = theChosenTile;
            } else {
                this.fishes[1][opfish++] = theChosenTile;
            }
        }

        JsonArray foods = msg.args.get(3).getAsJsonArray();
        Tile[] foodTiles = new Tile[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            JsonArray foodInfo = foods.get(i).getAsJsonArray();
            int id = foodInfo.get(0).getAsInt();
            int tileX = foodInfo.get(1).getAsInt();
            int tileY = foodInfo.get(2).getAsInt();

            Tile theChosenTile = map.getTile(tileX, tileY);
            theChosenTile.addItem(id, 0);
            foodTiles[i] = theChosenTile;

            idMap.put(id, theChosenTile);
            infoMap.put(id, theChosenTile.getItemInformation());
        }
        items[3] = foodTiles;

        JsonArray trashes = msg.args.get(4).getAsJsonArray();
        Tile[] trashTiles = new Tile[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            JsonArray trashInfo = trashes.get(i).getAsJsonArray();
            int id = trashInfo.get(0).getAsInt();
            int tileX = trashInfo.get(1).getAsInt();
            int tileY = trashInfo.get(2).getAsInt();

            Tile theChosenTile = tiles[tileX][tileY];
            theChosenTile.addItem(id, 1);
            trashTiles[i] = theChosenTile;

            idMap.put(id, theChosenTile);
            infoMap.put(id, theChosenTile.getItemInformation());
        }
        items[2] = trashTiles;

        JsonArray nets = msg.args.get(5).getAsJsonArray();
        Tile[] netTiles = new Tile[nets.size()];
        for (int i = 0; i < nets.size(); i++) {
            JsonArray netInfo = nets.get(i).getAsJsonArray();
            int id = netInfo.get(0).getAsInt();
            int tileX = netInfo.get(1).getAsInt();
            int tileY = netInfo.get(2).getAsInt();

            Tile theChosenTile = tiles[tileX][tileY];
            theChosenTile.addNet(id);
            netTiles[i] = theChosenTile;

            idMap.put(id, theChosenTile);
            infoMap.put(id, theChosenTile.getNetInformation());
        }
        items[1] = netTiles;

        JsonArray teleports = msg.args.get(6).getAsJsonArray();
        Tile[] teleportTiles = new Tile[teleports.size()];
        for (int i = 0; i < teleports.size(); i++) {
            JsonArray teleportInfo = teleports.get(i).getAsJsonArray();
            int id = teleportInfo.get(0).getAsInt();
            int tileX = teleportInfo.get(1).getAsInt();
            int tileY = teleportInfo.get(2).getAsInt();

            Tile theChosenTile = tiles[tileX][tileY];
            theChosenTile.addTeleport(id, teleportInfo.get(3).getAsInt());
            teleportTiles[i] = theChosenTile;

            idMap.put(id, theChosenTile);
            infoMap.put(id, theChosenTile.getTeleportInformation());
        }
        items[0] = teleportTiles;

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
            }

            char type = change.getType();
            if (type == 'a') { // add
                ArrayList<ArrayList<Integer>> allAdds = change.getArgs();
                for (int j = 0; j < allAdds.size(); j++) {
                    ArrayList<Integer> addChange = allAdds.get(j);
                    switch (addChange.get(1)) {
                        case 0:
                            addFish(addChange);
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
                    moveFish(moveChange);
                }
            } else if (type == 'c') { // change condition
                ArrayList<ArrayList<Integer>> allAlters = change.getArgs();
                for (int j = 0; j < allAlters.size(); j++) {
                    ArrayList<Integer> alter = allAlters.get(j);
                    if (alter.size() == 5) {
                        fishAlter(alter);
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

        Tile theChosenTile = idMap.get(id);
        Tile[][] tiles = map.getTiles();
        Tile targetTile = tiles[x][y];

        targetTile.receiveInfo(theChosenTile.getItemInformation());
        idMap.put(id, targetTile);
        theChosenTile.setItemInformation(null);
    }

    private void delete(ArrayList<Integer> changes) {
        int id = changes.get(0);

        Tile theChosenTile = idMap.get(id);
        idMap.remove(id);

        Information theChosenInfo = infoMap.get(id);
        infoMap.remove(id);

        if (theChosenInfo instanceof FishInformation) {
            FishInformation chosenFishInfo = (FishInformation) theChosenInfo;
            if (chosenFishInfo.getTeam() == teamID) {
                ArrayList<Tile> fishList = new ArrayList<Tile>(Arrays.asList(fishes[0]));
                fishList.remove(theChosenTile);
                Tile[] tempTile = new Tile[fishList.size()];
                fishes[0] = fishList.toArray(tempTile);
            } else {
                ArrayList<Tile> fishList = new ArrayList<Tile>(Arrays.asList(fishes[1]));
                fishList.remove(theChosenTile);
                Tile[] tempTile = new Tile[fishList.size()];
                fishes[1] = fishList.toArray(tempTile);
            }
            theChosenTile.clear();
        } else if (theChosenInfo instanceof ItemInformation && ((ItemInformation) theChosenInfo).getItemId() == 0) {
            ArrayList<Tile> foodList = new ArrayList<Tile>(Arrays.asList(items[3]));
            foodList.remove(theChosenTile);
            Tile[] tempTile = new Tile[foodList.size()];
            items[3] = foodList.toArray(tempTile);
            theChosenTile.clear();
        } else if (theChosenInfo instanceof ItemInformation && ((ItemInformation) theChosenInfo).getItemId() == 1) {
            ArrayList<Tile> trashList = new ArrayList<Tile>(Arrays.asList(items[2]));
            trashList.remove(theChosenTile);
            Tile[] tempTile = new Tile[trashList.size()];
            items[2] = trashList.toArray(tempTile);
            theChosenTile.clear();
        } else if (theChosenInfo instanceof NetInformation) {
            ArrayList<Tile> netList = new ArrayList<Tile>(Arrays.asList(items[1]));
            netList.remove(theChosenTile);
            Tile[] tempTile = new Tile[netList.size()];
            items[1] = netList.toArray(tempTile);
            theChosenTile.cleanNet();
        }
    }

    private void moveFish(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int move = changes.get(1);
        Tile theChosenTile = idMap.get(id);
        FishInformation theChosenInfo = (FishInformation) (infoMap.get(id));
        switch (move) {
            case 0:
                theChosenInfo.setDirection((theChosenInfo.getDirection() + 3) % 4);
                break;
            case 1:
                int tileX = nextX(theChosenTile, theChosenInfo.getDirection());
                int tileY = nextY(theChosenTile, theChosenInfo.getDirection());
                Tile targetTile = map.getTile(tileX, tileY);
                targetTile.receiveInfo(theChosenInfo);
                idMap.put(id, targetTile);
                if (targetTile.getFishInformation().getId() == theChosenTile.getFishInformation().getId()) {
                    theChosenTile.clear();
                }
                break;
            case 2:
                theChosenInfo.setDirection((theChosenInfo.getDirection() + 1) % 4);
                break;
        }
    }

    private int nextX(Tile tile, int dir) {
        int direction = dir;
        int x = tile.getX();
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

    private int nextY(Tile tile, int dir) {
        int direction = dir;
        int y = tile.getY();
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

    private void fishAlter(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int newX = changes.get(1);
        int newY = changes.get(2);
        int color = changes.get(3);
        int sick = changes.get(4);
        Tile theChosenTile = idMap.get(id);
        FishInformation theChosenInfo = (FishInformation) infoMap.get(id);
        Tile targetTile = map.getTile(newX, newY);
        theChosenInfo.setColor(color);
        theChosenInfo.setSick(sick);
        targetTile.receiveInfo(theChosenInfo);
        idMap.put(id, targetTile);
        if (targetTile != theChosenTile) {
            theChosenTile.clear();
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

    private void addFish(ArrayList<Integer> changes) {
        ArrayList<Tile> fishList;
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);
        int direction = changes.get(4);
        int color = changes.get(5);
        int queen = changes.get(6);
        int team = changes.get(7);

        Tile theChosenTile = map.getTile(tileX, tileY);
        theChosenTile.addFishInfo(id, direction, color, queen, team);

        idMap.put(id, theChosenTile);
        infoMap.put(id, theChosenTile.getFishInformation());

        if (team == teamID) {
            fishList = new ArrayList<Tile>(Arrays.asList(fishes[0]));
            fishList.add(theChosenTile);
            Tile[] tempTile = new Tile[fishList.size()];
            fishes[0] = fishList.toArray(tempTile);
        } else {
            fishList = new ArrayList<Tile>(Arrays.asList(fishes[1]));
            fishList.add(theChosenTile);
            Tile[] tempTile = new Tile[fishList.size()];
            fishes[1] = fishList.toArray(tempTile);
        }
    }

    private void addFood(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Tile theChosenTile = map.getTile(tileX, tileY);
        theChosenTile.addItem(id, 0);

        idMap.put(id, theChosenTile);
        infoMap.put(id, theChosenTile.getItemInformation());

        ArrayList<Tile> foodList = new ArrayList<Tile>(Arrays.asList(items[3]));
        foodList.add(theChosenTile);
        Tile[] tempTile = new Tile[foodList.size()];
        items[3] = foodList.toArray(tempTile);
    }

    private void addTrash(ArrayList<Integer> changes) {
//        Tile[][] tiles = map.getTiles();
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Tile theChosenTile = map.getTile(tileX, tileY);
        theChosenTile.addItem(id, 1);

        idMap.put(id, theChosenTile);
        infoMap.put(id, theChosenTile.getItemInformation());

        ArrayList<Tile> trashList = new ArrayList<Tile>(Arrays.asList(items[2]));
        trashList.add(theChosenTile);
        Tile[] tempTile = new Tile[trashList.size()];
        items[2] = trashList.toArray(tempTile);
    }

    private void addNet(ArrayList<Integer> changes) {
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);

        Tile theChosenTile = map.getTile(tileX, tileY);
        theChosenTile.addNet(id);

        idMap.put(id, theChosenTile);
        infoMap.put(id, theChosenTile.getNetInformation());

        ArrayList<Tile> netList = new ArrayList<Tile>(Arrays.asList(items[1]));
        netList.add(theChosenTile);
        Tile[] tempTile = new Tile[netList.size()];
        items[1] = netList.toArray(tempTile);
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public long totalTime() {
        return totalTime;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - startTime;
    }

    public long getRemainingTime() {
        return totalTime - getTimePassed();
    }

    public int getMyID() {
        return teamID;
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

    public int getQueenCollisionScore() {
        return queenCollisionScore;
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
        return totalTime;
    }

    public long getStartTime() {
        return startTime;
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

    public Tile[] getMyTiles() {
        return fishes[0];
    }

    public Tile[] getOppTiles() {
        return fishes[1];
    }

    public Tile[] getTeleportTiles() {
        return items[0];
    }

    public Tile[] getNetTiles() {
        return items[1];
    }

    public Tile[] getTrashTiles() {
        return items[2];
    }

    public Tile[] getFoodTiles() {
        return items[3];
    }

    public Map getMap() {
        return map;
    }

    public int getNetValidTime() {
        return netValidTime;
    }
}