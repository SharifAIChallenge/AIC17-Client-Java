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
     * Changing the strategy of the cockroaches with the given antenna type
     *
     * @param color Antenna type of the cockroach (0 for single antenna and 1 for double antenna)
     * @param right Condition of the top-right neighbour
     * @param front Condition of the front
     * @param left Condition of the top-left neighbour
     * @param strategy The command given to the cockroach (0 for turning right, 1 for going forward and 2 for turning left
     */
    void changeStrategy(int color, int right, int front, int left, int strategy);

    /**
     * Give command to a specific Cockroach with the given id
     *
     * @param id Cockroach id
     * @param s The command given to the cockroach(same as the previous method)
     */
    void deterministicMove(int id, int s);

    /**
     * Changes antenna type of the given cockroach
     *
     * @param id Cockroach id
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
     * Probability of a food showing up in any of the tiles without content(Cockroach or food)
     *
     * @return Food probability
     */
    double getFoodProb();

    /**
     * Probability of a trash showing up in any of the tiles without content(Cockroach or food)
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
     * Cost of changing the antenna of a cockroach
     *
     * @return Antenna cost
     */
    int getColorCost();

    /**
     * Sick cockroaches give points to the opposing team of all their 8 neighbours
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
     * @return Points achieved
     */
    int getKillQueenScore();

    /**
     *
     * @return
     */
    int getKillBothQueenScore();

    /**
     *
     * @return
     */
    int getKillFishScore();

    /**
     *
     * @return
     */
    int getQueenCollisionScore();

    /**
     *
     * @return
     */
    int getFishFoodScore();

    /**
     *
     * @return
     */
    int getQueenFoodScore();

    /**
     *
     * @return
     */
    int getSickLifeTime();

    /**
     *
     * @return
     */
    double getPowerRatio();

    /**
     *
     * @return
     */
    double getEndRatio();

    /**
     *
     * @return
     */
    int getDisobeyNum();

    /**
     *
     * @return
     */
    int getFoodValidTime();

    /**
     *
     * @return
     */
    int getTrashValidTime();

    /**
     *
     * @return
     */
    long getTotalTime();

    /**
     *
     * @return
     */
    long getStartTime();

    /**
     *
     * @return
     */
    int getTeamID();

    /**
     *
     * @return
     */
    int getWidth();

    /**
     *
     * @return
     */
    int getHeight();

    /**
     *
     * @return
     */
    int getMyScore();

    /**
     *
     * @return
     */
    int getOppScore();

    /**
     *
     * @return
     */
    Tile[] getMyTiles();

    /**
     *
     * @return
     */
    Tile[] getOppTiles();

    /**
     *
     * @return
     */
    Tile[] getTeleportTiles();

    /**
     *
     * @return
     */
    Tile[] getNetTiles();

    /**
     *
     * @return
     */
    Tile[] getTrashTiles();

    /**
     *
     * @return
     */
    Tile[] getFoodTiles();

    /**
     *
     * @param id
     * @return
     */
    FishInformation getFishInformation(int id);

    /**
     *
     * @param id
     * @return
     */
    ItemInformation getItemInformation(int id);

    /**
     *
     * @param id
     * @return
     */
    NetInformation getNetInformation(int id);

    /**
     *
     * @param id
     * @return
     */
    TeleportInformation getTeleportInformation(int id);

    /**
     *
     * @return
     */
    Map getMap();

    /**
     *
     * @return
     */
    int getNetValidTime();
}
