package client.main.moveHandler;

import client.main.map.mapNode.MapNode;
import client.main.moveHandler.pathFinding.PathFindingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MoveHandler {

    private final PathFindingController pathFindingController = new PathFindingController();
    private final CalculatorBetweenTwoPoints calculatorBetweenTwoPoints = new CalculatorBetweenTwoPoints();
    private ArrayList<Integer> movesThatNeedToBeMade;
    private ArrayList<Integer> temporaryMoves = new ArrayList<>();
    private boolean pathToOtherHalfMapCalculated = false;
    private final Logger logger = LoggerFactory.getLogger(MoveHandler.class);
    private int playerPositionOnBorder = 0;
    private boolean playerOnEnemyField = false;
    private final MountainChecker mountainChecker = new MountainChecker();

    public MoveHandler() {
    }

    public void initializeTreasureFinder(Collection<MapNode> mapNodeCollection, Point playerPosition) {
        Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> mapEntry =
                pathFindingController.calculatePathToGoal(mapNodeCollection, playerPosition);
        movesThatNeedToBeMade = mapEntry.getValue();
        calculatorBetweenTwoPoints.updateAdjListAndMovesList(mapEntry);
    }

    public void searchForEnemyCastle(Collection<MapNode> mapNodeCollection, Point playerPosition) {
        Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> mapEntry =
                pathFindingController.calculatePathToGoal(mapNodeCollection, playerPosition);
        clearMovements();
        movesThatNeedToBeMade = mapEntry.getValue();
        calculatorBetweenTwoPoints.updateAdjListAndMovesList(mapEntry);
    }

    public boolean isPlayerOnEnemyField() {
        return playerOnEnemyField;
    }

    public EMoveClient transitionToEnemyHalfMap(Collection<MapNode> mapNodeCollection, Point playerPosition) {
        if (!pathToOtherHalfMapCalculated)
            calculateMovesToEnemyHalfMap(mapNodeCollection, playerPosition);

        //Means we already are on the point we need to be
        if (movesThatNeedToBeMade.size() == 0) {
            if (playerPositionOnBorder == 0)
                playerPositionOnBorder = CoordinateTransformer.transformPointToIndex(playerPosition);
            if (playerPositionOnBorder != CoordinateTransformer.transformPointToIndex(playerPosition))
                playerOnEnemyField = true;
            return getMovesWhenOnBorder(CoordinateTransformer.transformPointToIndex(playerPosition));
        }
        if (movesThatNeedToBeMade.size() <= 2) {
            if (CoordinateTransformer.transformPointToIndex(playerPosition) == movesThatNeedToBeMade.get(movesThatNeedToBeMade.size() - 1)) {
                movesThatNeedToBeMade.clear();
                return getMovesWhenOnBorder(CoordinateTransformer.transformPointToIndex(playerPosition));
            }
        }
        return getNextMove(playerPosition);
    }

    private EMoveClient getMovesWhenOnBorder(int playerCoordinate) {
        int maxXCoordinateOnTopBorder = 9;
        if (playerCoordinate < maxXCoordinateOnTopBorder) {
            logger.info("Now on border, moving " + EMoveClient.UP);
            return EMoveClient.UP;
        }
        if (playerCoordinate % 10 == 0) {
            logger.info("Now on border, moving " + EMoveClient.LEFT);
            return EMoveClient.LEFT;
        }
        int minXCoordinateOnBottomBorder = 40;
        if (playerCoordinate > minXCoordinateOnBottomBorder) {
            logger.info("Now on border, moving " + EMoveClient.DOWN);
            return EMoveClient.DOWN;
        }
        logger.info("Now on border, moving " + EMoveClient.RIGHT);
        return EMoveClient.RIGHT;
    }

    private void calculateMovesToEnemyHalfMap(Collection<MapNode> mapNodeCollection, Point playerPosition) {
        clearMovements();
        pathToOtherHalfMapCalculated = true;
        int entryPoint = pathFindingController.getEntryPointToEnemyHalfMap(mapNodeCollection, playerPosition);
        //Player is already on the point that he would need to go to
        if (entryPoint == CoordinateTransformer.transformPointToIndex(playerPosition))
            return;
        movesThatNeedToBeMade = calculatorBetweenTwoPoints
                .getShortestDistance(CoordinateTransformer.transformPointToIndex(playerPosition), entryPoint, 50);
        logger.info("Calculated shortest path to enemy border: " + movesThatNeedToBeMade.toString());
    }

    private void clearMovements() {
        movesThatNeedToBeMade.clear();
        temporaryMoves.clear();
    }

    public EMoveClient getNextMove(Point playerPosition) {

        checkIfPlayerSearchedWholeMap(playerPosition);
        logger.info("Need to execute still: " + movesThatNeedToBeMade.toString());

        //If temporary moves is not empty it means that we are moving to a node which
        //is not directly connected with the current one
        if (!temporaryMoves.isEmpty()) {
            logger.info("Need to execute special moves: " + temporaryMoves);
            //If we are not on the first element of the list it means the player has moved on
            //and is currently on the 1 position of the list and we can remove the one we were one before
            if (CoordinateTransformer.transformPointToIndex(playerPosition) != temporaryMoves.get(0))
                temporaryMoves.remove(0);

            //If the size of temporary moves equals 1 it means that we have reached our end destination
            //and we can move on, doing so delete the list
            if (temporaryMoves.size() == 1)
                temporaryMoves.clear();
            else
                return getMoveBasedOnPosition(temporaryMoves.get(0), temporaryMoves.get(1));
        }

        //It means that the player already moved on from the last position so we remove it from the list
        if (CoordinateTransformer.transformPointToIndex(playerPosition) != movesThatNeedToBeMade.get(0))
            movesThatNeedToBeMade.remove(0);

        if (pointsDirectlyConnected(movesThatNeedToBeMade.get(0), movesThatNeedToBeMade.get(1)))
            return getMoveBasedOnPosition(movesThatNeedToBeMade.get(0), movesThatNeedToBeMade.get(1));

        calculateAlternativeRoute();
        return getMoveBasedOnPosition(temporaryMoves.get(0), temporaryMoves.get(1));
    }


    private void calculateAlternativeRoute() {
        temporaryMoves = calculatorBetweenTwoPoints.getShortestDistance(movesThatNeedToBeMade.get(0),
                movesThatNeedToBeMade.get(1), 50);
        logger.info("No direct way between " + movesThatNeedToBeMade.get(0) + " and " + movesThatNeedToBeMade.get(1) +
                ". Take alternate route: " + temporaryMoves.toString());
    }

    private void checkIfPlayerSearchedWholeMap(Point playerPosition) {
        if (CoordinateTransformer.transformPointToIndex(playerPosition) == movesThatNeedToBeMade.get(movesThatNeedToBeMade.size() - 1)) {
            logger.info("You already searched the whole map, quitting now.");
            System.exit(1);
        }
    }

    public void checkIfPlayerIsOnMountain(Collection<MapNode> fullMap, boolean isOnEnemyHalfMap) {
        if (mountainChecker.playerIsOnMountain(fullMap, isOnEnemyHalfMap)) {
            movesThatNeedToBeMade = (ArrayList<Integer>) mountainChecker.recalculatedRoute(movesThatNeedToBeMade);
            temporaryMoves.clear();
        }
    }

    protected int getMovesSize(){
        return movesThatNeedToBeMade.size();
    }

    protected EMoveClient getMoveBasedOnPosition(int startPoint, int endPoint) {
        if (startPoint + 1 == endPoint) {
            logger.info("Start point: " + startPoint + ", endpoint: " + endPoint + " moving: " + EMoveClient.RIGHT);
            return EMoveClient.RIGHT;
        }
        if (startPoint + 10 == endPoint) {
            logger.info("Start point: " + startPoint + ", endpoint: " + endPoint + " moving: " + EMoveClient.DOWN);
            return EMoveClient.DOWN;
        }
        if (startPoint - 1 == endPoint) {
            logger.info("Start point: " + startPoint + ", endpoint: " + endPoint + " moving: " + EMoveClient.LEFT);
            return EMoveClient.LEFT;
        }
        logger.info("Start point: " + startPoint + ", endpoint: " + endPoint + " moving: " + EMoveClient.UP);
        return EMoveClient.UP;
    }

    protected boolean pointsDirectlyConnected(int startPoint, int endPoint) {
        return startPoint + 10 == endPoint || startPoint - 10 == endPoint || startPoint - 1 == endPoint
                || startPoint + 1 == endPoint;
    }

    protected int getLastElementFromMovesThatNeedToBeMade(){
        return movesThatNeedToBeMade.get(movesThatNeedToBeMade.size()-1);
    }

    protected int getTempMovesSize(){
        return temporaryMoves.size();
    }

    protected void clearMovesThatNeedToBeMade(){
        movesThatNeedToBeMade.clear();
    }
    protected void addToMovesThatNeedToBeMade(int path){
        movesThatNeedToBeMade.add(path);
    }

}
