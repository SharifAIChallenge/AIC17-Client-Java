package client.model;

public class Information {
    private int id = -1;

    /*
     * These two fields are only used when we are
     * passing an Information object to user
     */
    private int x;
    private int y;

    public Information() {
    }

    public Information(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}