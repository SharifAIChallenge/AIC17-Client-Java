package client.model;

/**
 * Created by Future on 2/10/17.
 */
public enum Move {
    turnRight(0), stepForward(1), turnLeft(2);

    private final int value;
    Move(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
