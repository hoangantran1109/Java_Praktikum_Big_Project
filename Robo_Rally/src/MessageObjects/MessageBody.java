package MessageObjects;

import GameLogic.Player;
import java.util.List;

/**
 * class MessageBody
 *
 * This class defines all relevant message attributes (except sup. messageType)
 * of all messages provided according to the protocol.
 * Further description of certain message attributes can be found
 * in Message-implementing classes.
 */
public class MessageBody {

    Boolean isAI;
    Boolean ready;
    Boolean messageIsPrivate;
    Boolean connected;

    Integer to;
    Integer id;
    Integer figure;
    Integer phase;
    Integer position;
    Integer cardsInHand;
    Integer register;
    Integer count;
    Integer number;
    Integer cardsInPile;
    Integer from;

    String protocol;
    String group;
    String message;
    String error;
    String Action;
    String name;
    String card;
    String direction;

    int[] playerIDs;
    String[] cards;
    String[] availableMaps;
    String[] selectedMap;

    List<ActiveCard> activeCards;

    Player player;
    List<Map> map;

    public Boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public Boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Boolean isMessageIsPrivate() {
        return messageIsPrivate;
    }

    public void setMessageIsPrivate(boolean messageIsPrivate) {
        this.messageIsPrivate = messageIsPrivate;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Map> getMap() {
        return map;
    }

    public void setMap(List<Map> map) {
        this.map = map;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(int cards) {
        this.cardsInHand = cards;
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int[] getPlayerIDs() {
        return playerIDs;
    }

    public void setPlayerIDs(int[] playerIDs) {
        this.playerIDs = playerIDs;
    }

    public List<ActiveCard> getActiveCards() {
        return activeCards;
    }

    public void setActiveCards(List<ActiveCard> activeCards) {
        this.activeCards = activeCards;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String[] getAvailableMaps() {
        return availableMaps;
    }

    public void setAvailableMaps(String[] availableMaps) {
        this.availableMaps = availableMaps;
    }

    public String[] getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(String[] selectedMap) {
        this.selectedMap = selectedMap;
    }

    public Integer getCardsInPile() {
        return cardsInPile;
    }

    public void setCardsInPile(Integer cardsInPile) {
        this.cardsInPile = cardsInPile;
    }
}
