package ClientServer;

import GameLogic.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ludwig Kraus, Julian Wiemer, Alina Klessinger
 * Class Server
 * this class provides the server for the client server based chat
 */
public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    /*
     * All client names and figure IDs, so we can check for duplicates upon registration
     */
    private static final List<Player> playerList = Collections.synchronizedList(new ArrayList<>());

    private static int currentID = 1000;

    /*
     * The list of all the print writers for all the clients, used for broadcast.
     */
    private static final List<PrintWriter> chatUsers = new LinkedList<>();

    private static final List<Integer> userIds = new LinkedList<>();


    /**
     * main()
     *
     * @param args arguments from the commandline
     */
    public static void main(String[] args) {

        logger.info("Server is running");

        ExecutorService pool = Executors.newFixedThreadPool(500);

        try (ServerSocket listener = new ServerSocket(7070)) {
            while (true) {
                try {
                    pool.execute(new ClientHandler(listener.accept(), playerList, chatUsers, userIds, currentID));
                    currentID++;
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Player> getPlayerList() {
        return playerList;
    }
}

