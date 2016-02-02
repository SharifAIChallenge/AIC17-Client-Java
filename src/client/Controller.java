package client;

import client.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.network.data.Message;
import common.util.Json;
import common.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main controller. Controls execution of the program, e.g. checks time limit of
 * the client, handles incoming messages, controls network operations, etc.
 * This is an internal implementation and you do not need to know anything about
 * this class.
 * Please do not change this class.
 */
public class Controller {

    /**
     * Logging tag.
     */
    private static final String TAG = "Controller";

    /**
     * Connection details' file encoding.
     */
    private static String detailsEnc = "UTF-8";

    /**
     * Connection details.
     */
    private int port;
    private String host;
    private String token;
    private long retryDelay;

    /**
     * Player of the game.
     */
    private AI ai;

    /**
     * Model of the game.
     */
    private Model model;

    /**
     * Client side network.
     */
    private Network network;

    /**
     * Terminator. Controller waits for this object to be notified. Then it will
     * be terminated.
     */
    private final Object terminator;

    /**
     * Constructor
     *
     * @param hostIP        host address
     * @param hostPort      host port
     * @param token         client token
     * @param retryDelay    connection retry delay
     */
    public Controller(String hostIP, int hostPort, String token, long retryDelay) {
        this.terminator = new Object();
        this.host = hostIP;
        this.port = hostPort;
        this.token = token;
        this.retryDelay = retryDelay;
    }

    /**
     * Starts a client by connecting to the server and sending a token.
     */
    public void start() {
        try {
            network = new Network(this::handleMessage);
            model = new Model(network::send);
            ai = new AI();
            network.setConnectionData(host, port, token);
            while (!network.isConnected()) {
                network.connect();
                Thread.sleep(retryDelay);
            }
            synchronized (terminator) {
                terminator.wait();
            }
            network.terminate();
        } catch (Exception e) {
            Log.i(TAG, "Error while starting client.", e);
        }
    }

    /**
     * Handles incoming message. This method will be called from
     * {@link client.Network} when a new message is received.
     *
     * @param msg incoming message
     */
    private void handleMessage(Message msg) {
        switch (msg.name) {
            case "turn":
                handleTurnMessage(msg);
                break;
            case "init":
                handleInitMessage(msg);
                break;
            case "shutdown":
                handleShutdownMessage(msg);
                break;
            default:
                Log.i(TAG, "Undefined message received. " + msg.name);
                break;
        }
    }

    /**
     * Handles init message.
     *
     * @param msg init message
     */
    private void handleInitMessage(Message msg) {
        model.handleInitMessage(msg);
    }

    /**
     * Handles turn message. Gives the message to the model and then executes
     * client's code to do next turn.
     *
     * @param msg turn message
     */
    private void handleTurnMessage(Message msg) {
        model.handleTurnMessage(msg);
        doTurn();
    }

    /**
     * Handles shutdown message.
     *
     * @param msg shutdown message
     */
    private void handleShutdownMessage(Message msg) {
        network.terminate();
        System.exit(0);
    }

    /**
     * Starts {@link AI#doTurn} with turn timeout.
     */
    private void doTurn() {
        new Thread() {
            @Override
            public void run() {
                ai.doTurn(model);
            }
        }.start();
    }

}
