package client;

import client.model.*;

/**
 * Game Interface
 * At each turn, you are given an instance of the World, and you can call any of
 * the following methods to get information from the game, or do action on the
 * game. Please read documentation of any method you have problem with that.
 * Please do not change this class.
 */
public interface World {
    /**
     * Changing the strategy of the beetles with the given antenna type
     *
     * @param color Antenna type of the beetle (0 for single antenna and 1 for double antenna)
     * @param right Condition of the top-right neighbour
     * @param front Condition of the front
     * @param left Condition of the top-left neighbour
     * @param strategy The command given to the beetle (0 for turning right, 1 for going forward and 2 for turning left
     */

    void changeStrategy(int color, int right, int front, int left, int strategy);

    /**
     * Give command to a specific Beetle with the given id
     *
     * @param id Beetle id
     * @param s The command given to the beetle(same as the previous method)
     */
    void deterministicMove(int id, int s);

    /**
     * Changes antenna type of the given beetle
     *
     * @param id Beetle id
     * @param c Antenna type
     */
    void antennaChange(int id, int c);

    /**
     * Number of turns passed from the beginning of the game
     *
     * @return current turn number
     */
    int getCurrentTurn();

    /**
     *
     * @return Passed time(in milliseconds)
     */
    long getTotalTime();

    /**
     *
     * @return Team id
     */
    int getTeamID();

    /**
     *
     * @return width
     */
    int getWidth();

    /**
     *
     * @return height
     */
    int getHeight();

    /**
     *
     * @return User's score
     */
    int getMyScore();

    /**
     *
     * @return Opponent's score
     */
    int getOppScore();

    /**
     *
     * @return Cells which contains user's beetles
     */
    Cell[] getMyCells();

    /**
     *
     * @return Cells which contains opponent's beetles
     */
    Cell[] getOppCells();

    /**
     *
     * @return Cells which contains Teleporting devices
     */
    Cell[] getTeleportCells();

    /**
     *
     * @return Cells which contains net
     */
    Cell[] getNetCells();

    /**
     *
     * @return Cells which contains trash
     */
    Cell[] getTrashCells();

    /**
     *
     * @return Cells which contains food
     */
    Cell[] getFoodCells();

    /**
     *
     * @param id Beetle id
     * @return Entity object related to the given beetle
     */
    Beetle getBeetleInformation(int id);

    /**
     *
     * @param id Item id(food or trash)
     * @return Entity object related to the given item
     */
    ItemEntity getItemInformation(int id);

    /**
     *
     * @param id Net id
     * @return Entity object related to the given net
     */
    Slipper getNetInformation(int id);

    /**
     *
     * @param id Teleport device id
     * @return Entity object related to the given teleport device
     */
    Teleport getTeleportInformation(int id);

    int getTotalTurns();
    long getTurnRemainingTime();
    long getTurnTotalTime();
    Constants getConstants();


    /**
     *
     * @return Map of the game
     */
    Map getMap();

}
