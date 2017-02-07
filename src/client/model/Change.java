package client.model;

import java.util.ArrayList;

/**
 * Created by Future on 2/7/17.
 */
public class Change {
    private char type;
    private ArrayList<ArrayList<Integer>> args;

    public char getType() {
        return type;
    }

    public ArrayList<ArrayList<Integer>> getArgs() {
        return args;
    }
}
