package client;

/**
 * Initial point of execution.
 * Please do not change this class.
 */
public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception ignore) {
            printHelp();
        }
    }

    private static void run(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String token = args[2];
        long retryDelay = Long.parseLong(args[3]);
        try {
            new Controller(host, port, token, retryDelay).start();
        } catch (Exception e) {
            System.err.println("Client terminated with error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.err.println("Invalid arguments. You should provide host IP, host port, client token, and retry delay (in ms).");
    }

}