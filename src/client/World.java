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
     * Probability of a food showing up in any of the tiles without content(Beetle or food)
     *
     * @return Food probability
     */
    double getFoodProb();

    /**
     * Probability of a trash showing up in any of the tiles without content(Beetle or food)
     *
     * @return Trash probability
     */
    double getTrashProb();

    /**
     * Probability of a net showing up in any of the tiles without a net
     *
     * @return Net probability
     */
    double getNetProb();

    /**
     *
     * @return Maximum amount of turns
     */
    int getTurnTimeout();

    /**
     * Cost of changing the antenna of a beetle
     *
     * @return Antenna cost
     */
    int getColorCost();

    /**
     * Sick beetles give points to the opposing team of all their 8 neighbours
     *
     * @return Given points for each neighbour
     */
    int getSickCost();

    /**
     *
     * @return Cost of changing strategy
     */
    int getUpdateCost();

    /**
     *
     * @return Cost of a deterministic move
     */
    int getDetMoveCost();

    /**
     *
     * @return Points achieved when a normal beetle kills a queen
     */
    int getKillQueenScore();

    /**
     *
     * @return Points achieved when two queens kill each other
     */
    int getKillBothQueenScore();

    /**
     *
     * @return Points achieved from killing a normal beetle
     */
    int getKillFishScore();

    /**
     *
     * @return Points achieved when a normal beetle consumes food
     */
    int getFishFoodScore();

    /**
     *
     * @return Points achieved when a queen beetle consumes food
     */
    int getQueenFoodScore();

    /**
     *
     * @return Beetle's lifespan since getting sick
     */
    int getSickLifeTime();

    /**
     *
     * @return Amount of power that a beetle achieves each turn
     */
    double getPowerRatio();

    /**
     * If the map is "end ratio" percent full, the game ends
     *
     * @return End ratio
     */
    double getEndRatio();

    /**
     *
     * @return Minimum beetle population required for a beetle to disobey
     */
    int getDisobeyNum();

    /**
     *
     * @return Amount of turns a food stays on the map
     */
    int getFoodValidTime();

    /**
     *
     * @return Amount of turns a trash stays on the map
     */
    int getTrashValidTime();

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
     * @return Tiles which contains user's beetles
     */
    Cell[] getMyTiles();

    /**
     *
     * @return Tiles which contains opponent's beetles
     */
    Cell[] getOppTiles();

    /**
     *
     * @return Tiles which contains Teleporting devices
     */
    Cell[] getTeleportTiles();

    /**
     *
     * @return Tiles which contains net
     */
    Cell[] getNetTiles();

    /**
     *
     * @return Tiles which contains trash
     */
    Cell[] getTrashTiles();

    /**
     *
     * @return Tiles which contains food
     */
    Cell[] getFoodTiles();

    /**
     *
     * @param id Beetle id
     * @return Information object related to the given beetle
     */
    BeetleInformation getBeetleInformation(int id);

    /**
     *
     * @param id Item id(food or trash)
     * @return Information object related to the given item
     */
    ItemInformation getItemInformation(int id);

    /**
     *
     * @param id Net id
     * @return Information object related to the given net
     */
    NetInformation getNetInformation(int id);

    /**
     *
     * @param id Teleport device id
     * @return Information object related to the given teleport device
     */
    TeleportInformation getTeleportInformation(int id);

    /**
     *
     * @return Map of the game
     */
    Map getMap();

    /**
     *
     * @return Turns the net stays before hunting beetles
     */
    int getNetValidTime();
}
