import client.model.Map;
import client.model.Tile;
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
    private int currentTurn;
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
    private Tile[][] fishes = new Tile[2][]; // My normal-0, sick-1 and queen-2 fish tiles
    private HashMap<Integer, Tile> idMap = new HashMap<>();

    private Tile[][] freeTiles;

    private Consumer<Message> sender;

    private Map map;

    public Game(Consumer<Message> sender) {
        this.sender = sender;
    }



    public void changeStrategy(int color, int i, int j, int k, int s) {
        Event event = new Event("s", new Object[]{color, i, j, k, s});
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

        Tile[][] tiles = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }


        JsonArray fishes = msg.args.get(2).getAsJsonArray();
        this.fishes = new Tile[2][fishes.size()];
        int myfish = 0;
        int opfish = 0;
        for (int i = 0; i < fishes.size(); i++) {
            int tileX, tileY;
            JsonArray fishInfo = fishes.get(i).getAsJsonArray();
            tileX = fishInfo.get(1).getAsInt();
            tileY = fishInfo.get(2).getAsInt();
            tiles[tileX][tileY].addFishInfo(fishInfo);
            if (teamID == fishInfo.get(7).getAsInt()) {
                this.fishes[0][myfish++] = tiles[tileX][tileY];
            } else {
                this.fishes[1][opfish++] = tiles[tileX][tileY];
            }
        }

        JsonArray foods = msg.args.get(3).getAsJsonArray();
        Tile[] foodTiles = new Tile[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            JsonArray foodInfo = foods.get(i).getAsJsonArray();
            int id = foodInfo.get(0).getAsInt();
            int tileX = foodInfo.get(1).getAsInt();
            int tileY = foodInfo.get(2).getAsInt();
            tiles[tileX][tileY].resetConstants(id);
            foodTiles[i] = tiles[tileX][tileY];
        }
        items[3] = foodTiles;

        JsonArray trashes = msg.args.get(4).getAsJsonArray();
        Tile[] trashTiles = new Tile[trashes.size()];
        for (int i = 0; i < trashes.size(); i++) {
            JsonArray trashInfo = foods.get(i).getAsJsonArray();
            int id = trashInfo.get(0).getAsInt();
            int tileX = trashInfo.get(1).getAsInt();
            int tileY = trashInfo.get(2).getAsInt();
            tiles[tileX][tileY].resetConstants(id);
            trashTiles[i] = tiles[tileX][tileY];
        }
        items[2] = trashTiles;

        JsonArray nets = msg.args.get(5).getAsJsonArray();
        Tile[] netTiles = new Tile[nets.size()];
        for (int i = 0; i < nets.size(); i++) {
            JsonArray netInfo = nets.get(i).getAsJsonArray();
            int id = netInfo.get(0).getAsInt();
            int tileX = netInfo.get(1).getAsInt();
            int tileY = netInfo.get(2).getAsInt();
            tiles[tileX][tileY].resetConstants(id);
            netTiles[i] = tiles[tileX][tileY];
        }
        items[1] = netTiles;

        JsonArray teleports = msg.args.get(6).getAsJsonArray();
        Tile[] teleportTiles = new Tile[teleports.size()];
        for (int i = 0; i < teleports.size(); i++) {
            JsonArray teleportInfo = teleports.get(i).getAsJsonArray();
            int tileX = teleportInfo.get(1).getAsInt();
            int tileY = teleportInfo.get(2).getAsInt();
            tiles[tileX][tileY].addTeleport(teleportInfo);
            teleportTiles[i] = tiles[tileX][tileY];
        }
        items[0] = teleportTiles;

        JsonObject constants = msg.args.get(7).getAsJsonObject();
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
            String type = change.getType();
            if (type.equals("a")) {
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
            }
            else if(type.equals("d"))
            {

            }
            else if(type.equals("m"))
            {

            }
            else if(type.equals("c"))
            {
                ArrayList<ArrayList<Integer>> allAlters = change.getArgs();
                for (int j = 0; j < allAlters.size(); j++) {
                    ArrayList<Integer> alter = allAlters.get(j);
                    fishAlter(alter);
                }
            }
        }
    }

    private void fishAlter(ArrayList<Integer> changes)
    {
        int id = changes.get(0);
        int color = changes.get(1);
        int sick = changes.get(2);
        Tile theChosenTile = idMap.get(id);
        theChosenTile.setColor(color);
        theChosenTile.setSick(sick);
    }

    private void setConstants(JsonObject constants){
        turnTimeout = constants.getAsJsonPrimitive("turnTimeout").getAsInt();
        foodProb = constants.getAsJsonPrimitive("foodProb").getAsDouble();
        trashProb = constants.getAsJsonPrimitive("trashProb").getAsDouble();
        netProb = constants.getAsJsonPrimitive("netProb").getAsDouble();
        colorCost = constants.getAsJsonPrimitive("colorCost").getAsInt();
        sickCost = constants.getAsJsonPrimitive("sickCost").getAsInt();
        updateCost = constants.getAsJsonPrimitive("updateCost").getAsInt();
        detMoveCost = constants.getAsJsonPrimitive("detMoveCost").getAsInt();
        killQueenScore = constants.getAsJsonPrimitive("killQueenScore").getAsInt();
        killBothQueenScore = constants.getAsJsonPrimitive("killBothQueenScore").getAsInt();
        killFishScore = constants.getAsJsonPrimitive("killFishScore").getAsInt();
        queenCollisionScore = constants.getAsJsonPrimitive("queenCollisionScore").getAsInt();
        fishFoodScore = constants.getAsJsonPrimitive("fishFoodScore").getAsInt();
        queenFoodScore = constants.getAsJsonPrimitive("queenFoodScore").getAsInt();
        sickLifeTime = constants.getAsJsonPrimitive("sickLifeTime").getAsInt();
        powerRatio = constants.getAsJsonPrimitive("powerRatio").getAsDouble();
        endRatio = constants.getAsJsonPrimitive("endRatio").getAsDouble();
        disobeyNum = constants.getAsJsonPrimitive("disobeyNum").getAsInt();
        foodValidTime = constants.getAsJsonPrimitive("foodValidTime").getAsInt();
        trashValidTime = constants.getAsJsonPrimitive("trashValidTime").getAsInt();
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
        Tile[][] tiles = map.getTiles();
        Tile theChosenTile = tiles[tileX][tileY];
        theChosenTile.addFishInfo(id, direction, color, queen, team);
        if (team == teamID) {
            fishList = new ArrayList<Tile>(Arrays.asList(fishes[0]));
            fishList.add(theChosenTile);
            fishes[0] = (Tile[]) fishList.toArray();
        } else {
            fishList = new ArrayList<Tile>(Arrays.asList(fishes[1]));
            fishList.add(theChosenTile);
            fishes[1] = (Tile[]) fishList.toArray();
        }
    }

    private void addFood(ArrayList<Integer> changes) {
        Tile[][] tiles = map.getTiles();
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);
        Tile theChosenTile = tiles[tileX][tileY];
        theChosenTile.resetConstants(id);
        ArrayList<Tile> foodList = new ArrayList<Tile>(Arrays.asList(items[3]));
        foodList.add(theChosenTile);
        items[3] = (Tile[]) foodList.toArray();
    }

    private void addTrash(ArrayList<Integer> changes) {
        Tile[][] tiles = map.getTiles();
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);
        Tile theChosenTile = tiles[tileX][tileY];
        theChosenTile.resetConstants(id);
        ArrayList<Tile> trashList = new ArrayList<Tile>(Arrays.asList(items[2]));
        trashList.add(theChosenTile);
        items[2] = (Tile[]) trashList.toArray();
    }

    private void addNet(ArrayList<Integer> changes) {
        Tile[][] tiles = map.getTiles();
        int id = changes.get(0);
        int tileX = changes.get(2);
        int tileY = changes.get(3);
        Tile theChosenTile = tiles[tileX][tileY];
        theChosenTile.resetConstants(id);
        ArrayList<Tile> netList = new ArrayList<Tile>(Arrays.asList(items[1]));
        netList.add(theChosenTile);
        items[1] = (Tile[]) netList.toArray();
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


}