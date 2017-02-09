package client.model;

public class FishInformation extends ContentInformation {
    private int direction = -1;
    private int color = -1;
    private int queen = -1;
    private int sick = -1;
    private int team = -1;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getQueen() {
        return queen;
    }

    public void setQueen(int queen) {
        this.queen = queen;
    }

    public int getSick() {
        return sick;
    }

    public void setSick(int sick) {
        this.sick = sick;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}