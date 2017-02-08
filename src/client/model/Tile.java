package client.model;

import com.google.gson.JsonArray;

public class Tile {
//    public void moveFood
//    private String contentLevel = "";        //"trash"-"food"-"fish"-""
//    private int x;
//    private int y;
//    private int[] ids = {-1, -1, -1}; // content id-0, net id-1, teleport id-2
//    private int contentValue = -1; // fish -0, food-1, trash-2
//    private int direction = -1;
//    private int color = -1;
//    private int queen = -1;
//    private int sick = -1;
//    private int team = -1;
//    private int targetId = -1;
//    private boolean hasTeleport = false;
//    private boolean hasNet = false;

    private Information information = new Information();

    public Tile(int x, int y) {
        information.setX(x);
        information.setY(y);
    }

    public void addFishInfo(JsonArray fishInfo) {
        information.getIds()[0] = fishInfo.get(0).getAsInt();
        information.setDirection(fishInfo.get(3).getAsInt());
        information.setColor(fishInfo.get(4).getAsInt());
        information.setQueen(fishInfo.get(5).getAsInt());
        information.setSick(fishInfo.get(6).getAsInt());
        information.setTeam(fishInfo.get(7).getAsInt());
        information.setContentValue(0);
        information.setContentLevel("fish");
    }

    public void resetConstants(int id, int contentValue) {
        information.resetConstants(id, contentValue);
    }

    public void resetConstantsNet(int id) {
        information.resetConstantsNet(id);
    }

    public void addTeleport(JsonArray teleInfo) {
        int id = teleInfo.get(0).getAsInt();
        resetConstants(id, -1);
        information.setTargetId(teleInfo.get(3).getAsInt());
        information.setContentLevel("teleport");
    }

    public void addFishInfo(int id, int direction, int color, int queen, int team) {
        information.getIds()[0] = id;
        information.setDirection(direction);
        information.setColor(color);
        information.setQueen(queen);
        information.setTeam(team);
        information.setSick(0);
        information.setTargetId(-1);
        information.setContentLevel("fish");
        information.setContentValue(0);

    }

    public void recieveContent(/*Tile tile, int content*/Information information, int content) {
        int[] ids = this.information.getIds();
        if (content == -1) {
            this.getInformation().setHasNet(information.isHasNet());
            this.information.getIds()[1] = information.getIds()[1];
        }
        if (content == 0) {
            this.information.setContentLevel(information.getContentLevel());
            this.information.getIds()[0] = information.getIds()[0];
            this.information.setContentValue(information.getContentValue());
            this.information.setDirection(information.getDirection());
            this.information.setColor(information.getColor());
            this.information.setQueen(information.getQueen());
            this.information.setSick(information.getSick());
            this.information.setTeam(information.getTeam());
        } else if (content == 1 || content == 2) {
            this.information.setContentLevel(information.getContentLevel());
            this.information.getIds()[0] = information.getIds()[0];
            this.information.setContentValue(information.getContentValue());
        }
//        this.information = information;
//        this.information.setIds(ids);
//        tile.getIds()[content] = this.ids[content];
//        tile.setDirection(this.direction);
//        tile.setColor(this.color);
//        tile.setQueen(this.queen);
//        tile.setTeam(this.team);
//        tile.setSick(this.sick);
//        tile.setTargetId(this.targetId);
//        tile.setContentLevel(this.contentLevel);
//        tile.setContentValue(this.contentValue);
//        if (tile != this) {
//            resetConstants(-1, -1);
//        }

    }

    public void clear() {
        resetConstants(-1, -1);
    }

    public void cleanNet() {
        resetConstantsNet(-1);
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}