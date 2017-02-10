package client.model;

public class CockroachInformation extends ContentInformation {
    private int direction = -1;
    private int color = -1;
    private int queen = -1;
    private int sick = -1;
    private int team = -1;

    int getDirection() {
        return direction;
    }

    void setDirection(int direction) {
        this.direction = direction;
    }

    public int getColor() {
        return color;
    }

    void setColor(int color) {
        this.color = color;
    }

    public int getQueen() {
        return queen;
    }

    void setQueen(int queen) {
        this.queen = queen;
    }

    public int getSick() {
        return sick;
    }

    void setSick(int sick) {
        this.sick = sick;
    }

    int getTeam() {
        return team;
    }

    void setTeam(int team) {
        this.team = team;
    }
}