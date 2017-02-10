package client.model;

public class Entity {
    private int id = -1;

    /*
     * These two fields are only used when we are
     * passing an Entity object to user
     */
    private int x;
    private int y;

    Entity() {
    }

    Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }
}