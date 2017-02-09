package client;

import client.model.Map;
import client.model.Tile;

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
    public double getFoodProb();

    /**
     * Probability of a trash showing up in any of the tiles without content(Cockroach or food)
     *
     * @return Trash probability
     */
    public double getTrashProb();

    /**
     * Probability of a net showing up in any of the tiles without a net
     *
     * @return Net probability
     */
    public double getNetProb();

    /**
     *
     * @return Maximum amount of turns
     */
    public int getTurnTimeout();

    /**
     * Cost of changing the antenna of a cockroach
     *
     * @return Antenna cost
     */
    public int getColorCost();

    /**
     * Sick cockroaches give points to the opposing team of all their 8 neighbours
     *
     * @return Given points for each neighbour
     */
    public int getSickCost();

    /**
     *
     * @return Cost of changing strategy
     */
    public int getUpdateCost();

    /**
     *
     * @return Cost of a deterministic move
     */
    public int getDetMoveCost();

    /**
     *
     * @return Points achieved
     */
    public int getKillQueenScore();

    /**
     *
     * @return
     */
    public int getKillBothQueenScore();

    /**
     *
     * @return
     */
    public int getKillFishScore();

    /**
     *
     * @return
     */
    public int getQueenCollisionScore();

    /**
     *
     * @return
     */
    public int getFishFoodScore();

    /**
     *
     * @return
     */
    public int getQueenFoodScore();

    /**
     *
     * @return
     */
    public int getSickLifeTime();

    /**
     *
     * @return
     */
    public double getPowerRatio();

    /**
     *
     * @return
     */
    public double getEndRatio();

    /**
     *
     * @return
     */
    public int getDisobeyNum();

    /**
     *
     * @return
     */
    public int getFoodValidTime();

    /**
     *
     * @return
     */
    public int getTrashValidTime();

    /**
     *
     * @return
     */
    public long getTotalTime();

    /**
     *
     * @return
     */
    public long getStartTime();

    /**
     *
     * @return
     */
    public int getTeamID();

    /**
     *
     * @return
     */
    public int getWidth();

    /**
     *
     * @return
     */
    public int getHeight();

    /**
     *
     * @return
     */
    public int getMyScore();

    /**
     *
     * @return
     */
    public int getOppScore();

    /**
     *
     * @return
     */
    public Tile[] getMyTiles();

    /**
     *
     * @return
     */
    public Tile[] getOppTiles();

    /**
     *
     * @return
     */
    public Tile[] getTeleportTiles();

    /**
     *
     * @return
     */
    public Tile[] getNetTiles();

    /**
     *
     * @return
     */
    public Tile[] getTrashTiles();

    /**
     *
     * @return
     */
    public Tile[] getFoodTiles();

    /**
     *
     * @return
     */
    public Map getMap();

    /**
     *
     * @return
     */
    public int getNetValidTime();
}
