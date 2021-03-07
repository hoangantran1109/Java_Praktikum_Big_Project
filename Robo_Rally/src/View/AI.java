package View;

import ClientServer.Client;
import org.apache.logging.log4j.*;
import java.util.*;
import static java.lang.Thread.sleep;

/*
 * contains all AI functions
 * @author Simon Wiethase
 */
public class AI implements Runnable{

    private final Logger logger = LogManager.getLogger(AI.class);
    private final static AI instance = new AI();
    private AIWeights aiWeights;
    public static AI getInstance() {
        return instance;
    }
    private final Map<Integer, String> allWallsOnMap = new HashMap<>();
    private boolean isDizzyHighway = false;
    private boolean isExtraCrispy = false;

    /**
     * initialize AiWeight class, decide which map it is on and start to AI task
     */
    public void run() {

        aiWeights = new AIWeights();

        if (Context.getInstance().getMessage().getMessageBody().getMap().get(5).field.get(0).type.equals("empty")) {
            logger.info("AI detected gameBoard as DizzyHighway");
            isDizzyHighway = true;
            isExtraCrispy = false;
        } else {
            logger.info("AI detected gameBoard as ExtraCrispy");
            isDizzyHighway = false;
            isExtraCrispy = true;
        }
        try {
            AI.this.play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * used to notify the waiting AI
     */
    public synchronized void notifyAI(){
        notifyAll();
    }

    /**
     * runs indefinitely and sleeps until notify, calls AI functions
     * @throws InterruptedException
     */
    private synchronized void play() throws InterruptedException {

        logger.info("AI started...");

        for(var currentEntry : Context.getInstance().getMessage().getMessageBody().getMap()){
            for(var currentField : currentEntry.field){
                if(currentField.type.equals("wall") ){
                    allWallsOnMap.put(currentEntry.position, currentField.orientations.get(0));
                } else if(currentField.type.toLowerCase().equals("laser")){
                    allWallsOnMap.put(currentEntry.position, currentField.orientation);
                } else if(currentField.type.toLowerCase().equals("antenna")){
                    allWallsOnMap.put(currentEntry.position, "down");
                    allWallsOnMap.put(currentEntry.position-13, "down");
                    allWallsOnMap.put(currentEntry.position+1, "left");
                } else if(currentField.type.toLowerCase().equals("pit")){
                    allWallsOnMap.put(currentEntry.position, "down");
                    allWallsOnMap.put(currentEntry.position-13, "down");
                    allWallsOnMap.put(currentEntry.position+1, "left");
                    allWallsOnMap.put(currentEntry.position-1, "right");
                }
            }
        }

        while(Client.getInstance().connected){

            if (Context.getInstance().getPhase() == 0 && Client.getInstance().getPlayer().getId() == Context.getInstance().currentPlayer) {

                Context.getInstance().printGameMessage("AI sends starting position...");
                List<int[]> startPositions = Context.getInstance().getPossibleStartPositions();

                var takenStartingPositions = Client.getInstance().getTakenStartingPositions();
                int xToTake = 0;
                int yToTake = 0;

                for(int[] possibleStartPosition : startPositions){
                    if (takenStartingPositions.contains(possibleStartPosition[1] * 13 + possibleStartPosition[0])) {
                        continue;
                    }
                    xToTake = possibleStartPosition[0];
                    yToTake = possibleStartPosition[1];
                }

                Context.getInstance().sendStartingPoint(xToTake, yToTake);

                logger.info("AI did send Start Position");
            }
            if (Context.getInstance().getPhase() == 2) {

                sleep(1000);
                Map<Integer, String> allCards = Context.getInstance().getAllCardsNames();

                String[] selectedCards = chooseCards(allCards);

                /*
                 * simulate drawing from available cards as check
                 */

                List<String> availableCards = new ArrayList<>(Context.getInstance().getAllCardsNames().values());
                int actualCardNumber = 0;

                if(selectedCards != null){
                    for(int z = 0; z < 5; z++){
                        if(availableCards.contains(selectedCards[z])){
                            if(!selectedCards[z].equals("")){
                                actualCardNumber++;
                            }
                            availableCards.remove(selectedCards[z]);
                        } else {
                            throw new IllegalStateException("Calculation Error");
                        }
                    }
                }

                if(selectedCards == null || actualCardNumber != 5){
                    logger.info("Could not find any good path for the cards, taking the first 5");
                    selectedCards = new String[5];
                    for(int i = 0; i<5;i++){
                        selectedCards[i] = Context.getInstance().getAllCardsNames().get(i);
                    }
                }

                for(int i = 0; i<5;i++){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String cardName = selectedCards[i];
                    try {
                        Context.getInstance().sendCard(cardName,i);
                        Context.getInstance().setCardInProgrammingCards(cardName, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                logger.info("AI did send Cards");
            }

            if (Context.getInstance().getPhase() == 3) {

                sleep(1000);
                if(Context.getInstance().isDamageSelectable()) {

                    int repeatSelection = Context.getInstance().getCount();

                    for (int i = 0; i < repeatSelection; i++) {
                        int randomIndex = (int) (Math.random() * (4));
                        Context.getInstance().selectDamageCards(randomIndex);
                        logger.info("AI selected a Damage Card");
                    }
                    Context.getInstance().setDamageSelectable(false);
                }
            }
            logger.info("waiting...");
            this.wait();
        }
    }

    /**
     * @param allCards A KeyValue map of cards and its positions
     * the possible position with the highest score is selected
     * @return String[] bestCombination the best five card names
     */
    public String[] chooseCards(Map<Integer, String> allCards){

        int[] mapValues = new int[131];

        if(isDizzyHighway){
            mapValues = aiWeights.getMapValuesDizzyHighway();
        } else if(isExtraCrispy){
            logger.info("Current Checkpoint to reach is " + Context.getInstance().getCurrentCheckpoint());
            mapValues = aiWeights.getMapValuesExtraCrispy(Context.getInstance().getCurrentCheckpoint());
        }

        if(allCards.values().size() != 9){
            throw new IllegalStateException("Invalid number of available Cards");
        }

        Map<Integer, String> allEndPositionsFromPermutations = new HashMap<>();

        String[] elements = new String[9];
        int[] indexes = new int[9];
        for (int i = 0; i < 9; i++) {
            indexes[i] = 0;
        }

        for (int i = 0; i < 9; i++) {
            elements[i] = allCards.get(i);
        }

        Map<String, String[]> allPermutations = getAllPermutationsOfNine(elements, indexes);

        logger.info("total number of permutations is: " + allPermutations.size());

        int[] currentPositions = Context.getInstance().getRobotPositions().get(Client.getInstance().getPlayer().getFigure());
        int x = currentPositions[0];
        logger.info("x is: " + x);
        int y = currentPositions[1];
        logger.info("y is: " + y);

        int startPosition = y * 13 + x;

        logger.info("start position is : " + startPosition);

        Double orientation = Context.getInstance().getRobotOrientation().get(Client.getInstance().getPlayer().getFigure());

        String startDirection = "";
        if(orientation == 0) startDirection = "right";
        if(orientation == 90) startDirection = "right";
        if(orientation == 180) startDirection = "left";
        if(orientation == 270) startDirection = "up";

        logger.info("start direction is : " + startDirection);
        logger.info("possible end positions are:");

        for(String[] currentPermutations : allPermutations.values()){

            String[] currentPermutationsDeepCopy = new String[]{currentPermutations[0],currentPermutations[1],currentPermutations[2],currentPermutations[3],currentPermutations[4]};

            int currentEndPosition = getEndPositionOfFiveCards(currentPermutationsDeepCopy, startDirection ,startPosition, allWallsOnMap);
            
            if(mapValues != null && currentEndPosition > 0 && currentEndPosition < 130 && !allEndPositionsFromPermutations.containsKey(currentEndPosition)){
                allEndPositionsFromPermutations.put(currentEndPosition, currentPermutations[0]+currentPermutations[1]+currentPermutations[2]+currentPermutations[3]+currentPermutations[4]);
                logger.info("" + currentEndPosition + " with value " + mapValues[currentEndPosition]);
            }
        }
        if(allEndPositionsFromPermutations == null || allEndPositionsFromPermutations.size() == 0){
            return null;
        }
        int bestEndPositionValue = 0;
        int bestEndPosition = 0;
        for(int currentEndPosition : allEndPositionsFromPermutations.keySet()){
            if(mapValues[currentEndPosition] > bestEndPositionValue){
                bestEndPositionValue = mapValues[currentEndPosition];
                bestEndPosition = currentEndPosition;
            }
        }
        logger.info("\nCalculation finished, the best end position is: " + bestEndPosition + " with a value of: " + mapValues[bestEndPosition]);

        String bestEndPositionHash = allEndPositionsFromPermutations.get(bestEndPosition);
        String[] bestCombination = allPermutations.get(bestEndPositionHash);

        if(bestCombination == null || bestCombination.length == 0){
            return null;
        }

        logger.info("\nThe Hash is: " + bestEndPositionHash);
        logger.info("\nWith the cards: " + bestCombination[0]+" "+bestCombination[1]+" "+bestCombination[2]+" "+bestCombination[3]+" "+bestCombination[4]);


        for(String[] currentPermutations : allPermutations.values()) {

            String[] permutationCompare  = allPermutations.get(currentPermutations[0]+currentPermutations[1]+currentPermutations[2]+currentPermutations[3]+currentPermutations[4]);

            if(!(permutationCompare[0].equals(currentPermutations[0])
                &&permutationCompare[1].equals(currentPermutations[1])
                &&permutationCompare[2].equals(currentPermutations[2])
                &&permutationCompare[3].equals(currentPermutations[3])
                &&permutationCompare[4].equals(currentPermutations[4]))){

                throw new IllegalStateException("Error in Hash");
            }
        }
        return bestCombination;
    }

    /**
     * takes five cards that affect position and direction and combines them to a final position
     * @param elements the cards to combine into positions
     * @param startDirection the start direction to calculate from
     * @param currentPosition the start position to calculate from
     * @param wallsOnMap all walls on map
     * @return endPosition -1 if it is an impossible path, otherwise return the end position
     */
    public int getEndPositionOfFiveCards(String[] elements, String startDirection, int currentPosition, Map<Integer, String> wallsOnMap){

        int endPosition = currentPosition;

        String currentDirection = startDirection;

        //replace again with card left to it
        for(int i = 1; i<5; i++){
            if(elements[i].equals("Again")) elements[i] = elements[i-1];
        }

        for(String card : elements) {

            int currentY;
            if((endPosition % 13) == 0){
                currentY = (endPosition / 13)-1;
            } else {
                currentY = (endPosition / 13);
            }

            int currentX = (endPosition - (currentY * 13));

            switch (card) {
                case "BackUp" :
                    if(currentDirection.equals("left")) {
                        if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("right")
                            || wallsOnMap.containsKey(endPosition+1) && wallsOnMap.get(endPosition+1).equals("left")) return -1;
                        endPosition += 1;
                    }
                    if(currentDirection.equals("right")) {
                        if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("left")
                            || wallsOnMap.containsKey(endPosition-1) && wallsOnMap.get(endPosition-1).equals("right")) return -1;
                        endPosition -= 1;
                    }
                    if(currentDirection.equals("up")) {
                        if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("down")
                            || wallsOnMap.containsKey(endPosition+13) && wallsOnMap.get(endPosition+13).equals("up")) return -1;
                        endPosition += 13;
                    }
                    if(currentDirection.equals("down")) {
                        if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("up")
                            || wallsOnMap.containsKey(endPosition-13) && wallsOnMap.get(endPosition-13).equals("down")) return -1;
                        endPosition -= 13;
                    }
                    break;
                case "MoveI" : {
                    endPosition = moveInCurrentDirection(endPosition, currentDirection, 1, wallsOnMap);
                }
                    break;
                case "MoveII" : {
                    endPosition = moveInCurrentDirection(endPosition, currentDirection, 2, wallsOnMap);
                }
                    break;
                case "MoveIII" : {
                    endPosition = moveInCurrentDirection(endPosition, currentDirection, 3, wallsOnMap);
                }
                    break;
                case "TurnLeft" :
                    switch (currentDirection) {
                        case "left" -> currentDirection = "down";
                        case "right" -> currentDirection = "up";
                        case "up" -> currentDirection = "left";
                        case "down" -> currentDirection = "right";
                    }
                    break;
                case "TurnRight" :
                    currentDirection = switch (currentDirection) {
                        case "left" -> "up";
                        case "right" -> "down";
                        case "up" -> "right";
                        case "down" -> "left";
                        default -> currentDirection;
                    };
                    break;
                case "UTurn" :
                    currentDirection = switch (currentDirection) {
                        case "left" -> "right";
                        case "right" -> "left";
                        case "up" -> "down";
                        case "down" -> "up";
                        default -> currentDirection;
                    };
                    break;
            }

            int newY;
            if((endPosition % 13) == 0){
                newY = (endPosition / 13)-1;
            } else {
                newY = (endPosition / 13);
            }

            int newX = endPosition - (newY * 13);

            if(endPosition > 130 || endPosition < 1) return -99;

            if(currentDirection.equals("left") && currentY != newY) return -99;
            if(currentDirection.equals("right") && currentY != newY) return -99;

            if(currentDirection.equals("up") && currentX != newX) return -50;
            if(currentDirection.equals("down") && currentX != newX) return -50;
        }
        return endPosition;
    }

    /***
     * repeats positionChange x times according to number of steps and current direction
     * @param steps the number of steps to walk
     * @param currentDirection the start direction to calculate from
     * @param currentPosition the start position to calculate from
     * @return int endPosition the final position that is reached when combining five cards
     */
    private int moveInCurrentDirection(int currentPosition, String currentDirection, int steps, Map<Integer, String> wallsOnMap){
        int endPosition = currentPosition;
        for(int i = 0; i< steps; i++){
            if(currentDirection.equals("left")) {
                if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("left")
                    || wallsOnMap.containsKey(endPosition-1) && wallsOnMap.get(endPosition-1).equals("right")) return -1;
                endPosition -= 1;
            }
            if(currentDirection.equals("right")) {
                if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("right")
                    || wallsOnMap.containsKey(endPosition+1) && wallsOnMap.get(endPosition+1).equals("left")) return -1;
                endPosition += 1;
            }
            if(currentDirection.equals("up")) {
                if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("up")
                    || wallsOnMap.containsKey(endPosition-13) && wallsOnMap.get(endPosition-13).equals("down")) return -1;
                endPosition -= 13;
            }
            if(currentDirection.equals("down")) {
                if(wallsOnMap.containsKey(endPosition) && wallsOnMap.get(endPosition).equals("down")
                    || wallsOnMap.containsKey(endPosition+13) && wallsOnMap.get(endPosition+13).equals("up")) return -1;
                endPosition += 13;
            }
        }
        return endPosition;
    }

    /**
     * adds all possible combinations of a five strings to the global list of combinations
     * @param elements all cards to select five from
     * @param indexes empty indexes to populate
     * @return Map<String, String[]> allPermutations all possible combinations to select five strings out of nine
     */
    private Map<String, String[]> getAllPermutationsOfNine(String[] elements, int[] indexes)
    {
        Map<String, String[]> allPermutations = new HashMap<>();
        int i = 0;
        if(elements.length != 9){
            throw new IllegalStateException("Invalid number of available Cards");
        }
        int repetitions = 0;
        while (i < 9 && repetitions < 10000) {
            repetitions++;
            if (indexes[i] < i) {
                if (!elements[i % 2 == 0 ? 0 : indexes[i]].equals(elements[i])) {
                    swap(elements, i % 2 == 0 ? 0 : indexes[i], i);
                    String permutationHash = elements[0]+elements[1]+elements[2]+elements[3]+elements[4];

                    if(!allPermutations.containsKey(permutationHash)){
                        allPermutations.put(permutationHash, new String[]{elements[0],elements[1],elements[2],elements[3],elements[4]});
                    }
                }
                indexes[i]++;
                i = 0;
            } else {
                indexes[i] = 0;
                i++;
            }
        }
        return allPermutations;
    }

    /**
     * swaps two integers at the position of a provided array of integers
     * @param input the array to be modified
     * @param a one of two values that will be swapped
     * @param b one of two values that will be swapped
     */
    private void swap(String[] input, int a, int b) {
        String tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
}
