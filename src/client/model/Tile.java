package client.model;

import com.google.gson.JsonArray;

public class Tile {
    private int x;
    private int y;
	private Information fishInformation;
	private Information itemInformation;
	private Information netInformation;
	private Information teleportInformation;

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
			fishInformation = information;
		}
		else if (information instanceof ItemInformation)
		{
			itemInformation = information;
		}
		else if (information instanceof NetInformation)
		{
			netInformation = information;
		}
		else if (information instanceof TeleportInformation)
		{
			teleportInformation = information;
		}
	}

    public void addFishInfo(JsonArray fishInfo) {
		fishInformation = new FishInformation();
		fishInformation.setId(fishInfo.get(0).getAsInt());
		FishInformation chosenFishInfo = (FishInformation) fishInformation;
        chosenFishInfo.setDirection(fishInfo.get(3).getAsInt());
        chosenFishInfo.setColor(fishInfo.get(4).getAsInt());
        chosenFishInfo.setQueen(fishInfo.get(5).getAsInt());
        chosenFishInfo.setSick(fishInfo.get(6).getAsInt());
        chosenFishInfo.setTeam(fishInfo.get(7).getAsInt());
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

    public void addFishInfo(int id, int direction, int color, int queen, int team) {
        fishInformation = new FishInformation();
		fishInformation.setId(id);
        FishInformation chosenFishInfo = (FishInformation) fishInformation;
        chosenFishInfo.setDirection(direction);
        chosenFishInfo.setColor(color);
        chosenFishInfo.setQueen(queen);
        chosenFishInfo.setSick(0);
        chosenFishInfo.setTeam(team);
    }

    public void clear() {
		fishInformation = null;
		itemInformation = null;
    }

    public void cleanNet() {
		netInformation = null;
    }

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