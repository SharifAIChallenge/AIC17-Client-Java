package client.model;

import com.google.gson.JsonArray;

public class Tile {
//    public void moveFood
//    private String contentLevel = "";        //"trash"-"food"-"fish"-""
    private int x;
    private int y;
//    private int targetId = -1;
//    private boolean hasTeleport = false;
	private Information fishInformation;
	private Information itemInformation;
	private Information netInformation;
	private Information teleportInformation;
//    private int[] ids = {-1, -1, -1}; // content id-0, net id-1, teleport id-2
//    private int contentValue = -1; // fish -0, food-1, trash-2
//    private int direction = -1;
//    private int color = -1;
//    private int queen = -1;
//    private int sick = -1;
//    private int team = -1;
//    private boolean hasNet = false;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
	
	public void addItem(int id, int itemId)
	{
		itemInformation = new ItemInformation(id, itemId);
	}
	
	public void addNet(int id)
	{
		netInformation = new NetInformation(id);
	}
	
	public void addTeleport(int id, int targetId)
	{
		teleportInformation = new TeleportInformation(id, targetId);
	}
	
	public void receiveInfo(Information information)
	{
		if(information instanceof FishInformation)
		{
			fishInformation = (FishInformation) information;
		}
		else if (information instanceof ItemInformation)
		{
			itemInformation = (ItemInformation) information;
		}
		else if (information instanceof NetInformation)
		{
			netInformation = (NetInformation) information;
		}
		else if (information instanceof TeleportInformation)
		{
			teleportInformation = (TeleportInformation) information;
		}
	}

    public void addFishInfo(JsonArray fishInfo) {
		fishInformation = new FishInformation();
		fishInformation.setId(fishInfo.get(0).getAsInt());
        ((FishInformation) fishInformation).setDirection(fishInfo.get(3).getAsInt());
        ((FishInformation) fishInformation).setColor(fishInfo.get(4).getAsInt());
        ((FishInformation) fishInformation).setQueen(fishInfo.get(5).getAsInt());
        ((FishInformation) fishInformation).setSick(fishInfo.get(6).getAsInt());
        ((FishInformation) fishInformation).setTeam(fishInfo.get(7).getAsInt());
		
        /*ids[0] = fishInfo.get(0).getAsInt();
        direction = fishInfo.get(3).getAsInt();
        color = fishInfo.get(4).getAsInt();
        queen = fishInfo.get(5).getAsInt();
        sick = fishInfo.get(6).getAsInt();
        team = fishInfo.get(7).getAsInt();
        contentValue = 0;
        this.contentLevel = "fish";*/
    }

//    public int[] getIds() {
//        return ids;
//    }
//
//    public void setIds(int[] ids) {
//        this.ids = ids;
//    }
//
//    public int getDirection() {
//        return direction;
//    }
//
//    public void setDirection(int direction) {
//        this.direction = direction;
//    }
//
//    public int getColor() {
//        return color;
//    }
//
//    public void setColor(int color) {
//        this.color = color;
//    }
//
//    public int getQueen() {
//        return queen;
//    }
//
//    public void setQueen(int queen) {
//        this.queen = queen;
//    }
//
//    public int getSick() {
//        return sick;
//    }
//
//    public void setSick(int sick) {
//        this.sick = sick;
//    }
//
//    public int getTeam() {
//        return team;
//    }
//
//    public void setTeam(int team) {
//        this.team = team;
//    }

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

//    public String getContentLevel() {
//        return contentLevel;
//    }
//
//    public void setContentLevel(String contentLevel) {
//        this.contentLevel = contentLevel;
//    }
//
//    public int getTargetId() {
//        return targetId;
//    }
//
//    public void setTargetId(int targetId) {
//        this.targetId = targetId;
//    }
//
//    public int getContentValue() {
//        return contentValue;
//    }
//
//    public void setContentValue(int contentValue) {
//        this.contentValue = contentValue;
//    }

//    public void resetConstants(int id, int contentValue) {
//        this.ids[0] = id;
//        this.direction = -1;
//        this.color = -1;
//        this.queen = -1;
//        this.sick = -1;
//        this.team = -1;
//        this.targetId = -1;
//        this.contentLevel = "";
//        this.contentValue = contentValue;
//    }

//    public void resetConstantsNet(int id) {
//        this.ids[1] = id;
//        this.direction = -1;
//        this.color = -1;
//        this.queen = -1;
//        this.sick = -1;
//        this.team = -1;
//        this.targetId = -1;
//        this.contentValue = -1;
//    }

//    public void addTeleport(JsonArray teleInfo) {
//        int id = teleInfo.get(0).getAsInt();
//        resetConstants(id, -1);
//        targetId = teleInfo.get(3).getAsInt();
//        this.contentLevel = "teleport";
//    }

    public void addFishInfo(int id, int direction, int color, int queen, int team) {
        fishInformation = new FishInformation();
		fishInformation.setId(id);
        ((FishInformation) fishInformation).setDirection(direction);
        ((FishInformation) fishInformation).setColor(color);
        ((FishInformation) fishInformation).setQueen(queen);
        ((FishInformation) fishInformation).setSick(0);
        ((FishInformation) fishInformation).setTeam(team);
		
		
		/*this.ids[0] = id;
        this.direction = direction;
        this.color = color;
        this.queen = queen;
        this.team = team;
        this.sick = 0;
        this.targetId = -1;
        this.contentLevel = "fish";
        this.contentValue = 0;*/
    }

//    public void moveContent(Tile tile, int content) {
//
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
//    }

    public void clear() {
        //resetConstants(-1, -1);
		fishInformation = null;
		itemInformation = null;
    }

    public void cleanNet() {
        //resetConstantsNet(-1);
		netInformation = null;
    }

//    public boolean isHasTeleport() {
//        return hasTeleport;
//    }
//
//    public void setHasTeleport(boolean hasTeleport) {
//        this.hasTeleport = hasTeleport;
//    }
//
//    public boolean isHasNet() {
//        return hasNet;
//    }
//
//    public void setHasNet(boolean hasNet) {
//        this.hasNet = hasNet;
//    }

    public Information getFishInformation() {
        return fishInformation;
    }

    public void setFishInformation(Information fishInformation) {
        this.fishInformation = fishInformation;
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