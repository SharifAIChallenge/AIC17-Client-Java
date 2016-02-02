package client.model;

import client.World;
import com.google.gson.JsonArray;
import common.model.Event;
import common.network.data.Message;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Model contains data which describes current state of the game.
 * You do not need to read this class, it's internal implementation. View
 * World interface for more information.
 * Please do not change this class.
 */
public class Model implements World {
    private long turnTimeout = 400; // todo
    private long turnStartTime;
    private Consumer<Message> sender;

    private int myID;
    private int turn;
    private Graph map;

    private Node[][] nodes = new Node[3][]; // free nodes, player1's nodes, player2's nodes

    private class NodeArrayList extends ArrayList<Node> {
    } // solving generic array creation!

    public Model(Consumer<Message> sender) {
        this.sender = sender;
    }

    public void handleInitMessage(Message msg) {
        myID = (int) msg.args[0];

        // graph deserialization
        JsonArray adjListInt = (JsonArray) msg.args[1];

        Node[] nodes = new Node[adjListInt.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < adjListInt.size(); i++) {
            JsonArray neighboursInt = adjListInt.get(i).getAsJsonArray();
            Node[] neighbours = new Node[adjListInt.get(i).getAsJsonArray().size()];
            for (int j = 0; j < neighbours.length; j++) {
                neighbours[j] = nodes[neighboursInt.get(j).getAsInt()];
            }
            nodes[i].setNeighbours(neighbours);
        }

        JsonArray graphDiff = (JsonArray) msg.args[2];
        for (int i = 0; i < graphDiff.size(); i++) {
            JsonArray nodeDiff = graphDiff.get(i).getAsJsonArray();
            int node = nodeDiff.get(0).getAsInt();
            int owner = nodeDiff.get(1).getAsInt();
            int armyCount = nodeDiff.get(2).getAsInt();
            nodes[node].setOwner(owner);
            nodes[node].setArmyCount(armyCount);
        }

        updateNodesList();

        map = new Graph(nodes);
    }

    public void handleTurnMessage(Message msg) {
        turnStartTime = System.currentTimeMillis();
        turn = (int) msg.args[0];

        JsonArray graphDiff = (JsonArray) msg.args[1];
        for (int i = 0; i < graphDiff.size(); i++) {
            JsonArray nodeDiff = graphDiff.get(i).getAsJsonArray();
            int nodeIndex = nodeDiff.get(0).getAsInt();
            map.getNode(nodeIndex).setOwner(nodeDiff.get(1).getAsInt());
            map.getNode(nodeIndex).setArmyCount(nodeDiff.get(2).getAsInt());
        }

        updateNodesList();
    }

    private void updateNodesList() {
        NodeArrayList[] nodesList = new NodeArrayList[]{new NodeArrayList(), new NodeArrayList(), new NodeArrayList()};
        for (Node n : map.getNodes()) {
            nodesList[n.getOwner() + 1].add(n);
        }
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = nodesList[i].toArray(new Node[nodesList[i].size()]);
        }
    }

    public long getTurnTimePassed() {
        return System.currentTimeMillis() - turnStartTime;
    }

    public long getTurnRemainingTime() {
        return turnTimeout - getTurnTimePassed();
    }

    @Override
    public int getMyID() {
        return myID;
    }

    @Override
    public Graph getMap() {
        return map;
    }

    @Override
    public Node[] getMyNodes() {
        return nodes[myID + 1];
    }

    @Override
    public Node[] getOpponentNodes() {
        return nodes[2 - myID];
    }

    @Override
    public Node[] getFreeNodes() {
        return nodes[0];
    }

    @Override
    public int getTurnNumber() {
        return turn;
    }

    @Override
    public long getTotalTurnTime() {
        return turnTimeout;
    }

    @Override
    public void moveArmy(int src, int dst, int count) {
        sender.accept(new Message(Event.EVENT, new Object[]{
                new Event("m", new Object[]{src, dst, count})
        }));
    }

}
