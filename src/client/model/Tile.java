package client.model;

import com.google.gson.JsonArray;

public class Tile
{
	private String kind;		//"teleport"-"net"-"trash"-"food"-"fish"-""
	private int x;
	private int y;
	private int id = -1;
	private int direction = -1;
	private int color = -1;
	private int queen = -1;
	private int sick = -1;
	private int team = -1;
	private int targetId = -1;
	
	public Tile(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void addFishInfo(JsonArray fishInfo) {
		id = fishInfo.get(0).getAsInt();
		direction = fishInfo.get(3).getAsInt();
		color = fishInfo.get(4).getAsInt();
		queen = fishInfo.get(5).getAsInt();
		sick = fishInfo.get(6).getAsInt();
		team = fishInfo.get(7).getAsInt();
		this.setKind("fish");
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public void setColor(int color)
	{
		this.color = color;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void setQueen(int queen)
	{
		this.queen = queen;
	}
	
	public int getQueen()
	{
		return queen;
	}
	
	public void setSick(int sick)
	{
		this.sick = sick;
	}
	
	public int getSick()
	{
		return sick;
	}
	
	public void setTeam(int team)
	{
		this.team = team;
	}
	
	public int getTeam()
	{
		return team;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public void resetConstants(int id) {
		this.id = id;
		this.direction = -1;
		this.color = -1;
		this.queen = -1;
		this.sick = -1;
		this.team = -1;
		this.targetId = -1;
		this.kind = "";
	}

	public void addTeleport(JsonArray teleInfo){
		int id = teleInfo.get(0).getAsInt();
		resetConstants(id);
		targetId = teleInfo.get(3).getAsInt();
		this.setKind("teleport");
	}

	public void addFishInfo(int id, int direction, int color, int queen, int team) {
		this.id = id;
		this.direction = direction;
		this.color = color;
		this.queen = queen;
		this.team = team;
		this.sick = -1;
		this.targetId = -1;
		this.setKind("fish");
	}

	public void move(Tile tile) {
		tile.setId(this.id);
		tile.setDirection(this.direction);
		tile.setColor(this.color);
		tile.setQueen(this.queen);
		tile.setTeam(this.team);
		tile.setSick(this.sick);
		tile.setTargetId(this.targetId);
		tile.setKind(this.kind);
	}
}