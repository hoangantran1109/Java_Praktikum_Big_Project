/**
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class GameSendingMethods
 */

package GameLogic;

import MessageObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameSendingMethods {

    public static Gson gson;
    private static int MESSAGE_DELAY;

    /**
     * initializeGameSendingMethods()
     *
     * @param delay intializes the gsonBuilder and sets the delay the passes before a message is sent
     */
    public static void initializeGameSendingMethods(int delay) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();
        MESSAGE_DELAY = delay;
    }

    /**
     * sendDrawDamageMessage()
     *
     * @param victim              player who gets damageCards
     * @param kindOfDamageCard
     * @param amountOfDamageCards sends DrawDamage message with a certain kind of damage card and a certain amount of these cards
     */
    public static void sendDrawDamageMessage(Player victim, String kindOfDamageCard, int amountOfDamageCards) {
        String[] damageCards = new String[amountOfDamageCards];
        for (int i = 0; i < amountOfDamageCards; i++) {
            damageCards[i] = kindOfDamageCard;
        }
        Message drawDamageMessage = new DrawDamage(victim.getId(), damageCards);
        sendToAll(gson.toJson(drawDamageMessage));
    }

    /**
     * sendPickDamageMessage()
     *
     * @param player        who has to choose damageCards
     * @param cardsToChoose sends PickDamage message to a player who has to choose a certain amount of damageCards
     */
    public static void sendPickDamageMessage(Player player, int cardsToChoose) {
        Message pickDamageMessage = new PickDamage(cardsToChoose);
        sendToSinglePlayer(player, gson.toJson(pickDamageMessage));
    }

    /**
     * sendShuffleCodingMessage()
     *
     * @param player whose programmingDeck is refilled and shuffled
     *               sends ShuffleCoding message to a player
     */
    public static void sendShuffleCodingMessage(Player player) {
        Message shuffleCodingMessage = new ShuffleCoding(player.getId());
        sendToAll(gson.toJson(shuffleCodingMessage));
    }

    /**
     * sendPlayerShootingMessage()
     * sends PlayerShooting message to all players when the robotLasers activate
     */
    public static void sendPlayerShootingMessage() {
        Message playerShootingMessage = new PlayerShooting();
        sendToAll(gson.toJson(playerShootingMessage));
    }

    /**
     * sendPlayerTurningMessage()
     * @param player
     * @param clockwise
     * @param sendWithDelay
     * sends PlayerTurning message with or without delay when a robot on the gameboard turns clockwise or counterclockwise
     */
    public static void sendPlayerTurningMessage(Player player, Boolean clockwise, boolean sendWithDelay) {
        String direction;
        if (clockwise) {
            direction = "clockwise";
        } else {
            direction = "counterClockwise";
        }
        Message playerTurningMessage = new PlayerTurning(player.getId(), direction);
        sendToAll(gson.toJson(playerTurningMessage));
        if (sendWithDelay) {
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sendMoveMessage()
     * @param x_position
     * @param y_position
     * @param sendWithDelay
     * sends MoveMessage message with or without delay when a robot on the gameboard moves to another field
     */
    public static void sendMoveMessage(int x_position, int y_position, int id, boolean sendWithDelay) {
        int parsedPosition = x_position + 13 * y_position;
        Message sendMoveMessage = new Movement(id, parsedPosition);
        sendToAll(gson.toJson(sendMoveMessage));
        if (sendWithDelay) {
            try {
                Thread.sleep(MESSAGE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sendRebootMessage()
     * @param id
     * sends RebootMessage message with or without delay with the id of a player whose robot reboots
     */
    public static void sendRebootMessage(int id) {
        Message rebootMessage = new Reboot(id);
        sendToAll(gson.toJson(rebootMessage));
        try {
            Thread.sleep(MESSAGE_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendCurrentCards()
     * @param register
     * sends CurrentCards message that contains the cards of all players in a certain register
     */
    public static void sendCurrentCards(int register) {
        List<ActiveCard> activeCards = new ArrayList<>();
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {
                if (player.getProgrammingCardRegister().get(register) != null) {
                    ActiveCard activeCard = new ActiveCard(player.getId(), player.getProgrammingCardRegister().get(register));
                    activeCards.add(activeCard);
                }
            }
        }
        Message currentCardsMessage = new CurrentCards(activeCards);
        sendToAll(gson.toJson(currentCardsMessage));
    }

    /**
     * sendToAll()
     * @param message
     * sends message to all players
     */
    public static void sendToAll(String message) {
        for (Player p : RoboRallyGame.getPlayersInGame()) {
            sendToSinglePlayer(p, message);
        }
    }

    /**
     * sendToSinglePlayer()
     *
     * @param messageReceiver the player who will receive the message
     * @param message         the text body
     * sends a message to only one player
     */
    public static void sendToSinglePlayer(Player messageReceiver, String message) {
        messageReceiver.getOut().println(message);
    }
}
