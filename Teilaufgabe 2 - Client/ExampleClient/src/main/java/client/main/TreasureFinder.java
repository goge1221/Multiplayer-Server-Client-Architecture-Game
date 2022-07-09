package client.main;

import MessagesBase.MessagesFromClient.EMove;
import MessagesBase.MessagesFromClient.HalfMapNode;
import client.main.FullMap.FullMapController;
import client.main.PathFinder.CoordinateTransformer;
import client.main.Player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class TreasureFinder {

    private CoordinateTransformer coordinateTransformer = new CoordinateTransformer();
    private boolean firstIteration = true;

    private EMove moveToExecuteAgain;
    private Point currentPosition;
    private ArrayList<Integer>movesThatNeedToBeMade = new ArrayList<>();

    private boolean gettingToAPosition = false;
    private HashSet<Integer>movesAlreadyMade = new HashSet<>();
    private ArrayList<Integer>movesToExecute = new ArrayList<>();

    private FullMapController fullMapController;

    public EMove getNextMove(Player player, FullMapController fullMapController, Collection<HalfMapNode> mapNodes) {
        this.fullMapController = fullMapController;
        int playerCoordinate = coordinateTransformer.transformPlayerCoordinate((int) player.getPosition().getX(), (int) player.getPosition().getY());

            if(firstIteration) {
                initializeGraph(playerCoordinate, player);
            }

            System.out.println("Moves that need to be made: " + movesThatNeedToBeMade);

            //Falls ein spieler noch nicht an diese position war dann lösch diese position
        movesAlreadyMade.add(playerCoordinate);


        //HERE ADD A FUNCTION THAT EXECUTES IF PLAYER SHOULD MOVE TO A CERTAIN POSITION
        if(gettingToAPosition){
            System.out.println("Getting to a position was activ. ");
            int proposedPosition = movesToExecute.get(movesToExecute.size()-1);
            if(playerCoordinate == proposedPosition){
                gettingToAPosition = false;
                movesToExecute.clear();
            }
            else{
                //execute remaining moves
                movesAlreadyMade.add(playerCoordinate);
                if(playerCoordinate == movesToExecute.get(0)) movesToExecute.remove(0);
                int next = 0;
                if(!movesToExecute.isEmpty()) next = movesToExecute.get(0);
                System.out.println("Moves to execute: " + movesToExecute);
                return calculateNextMove(player.getPosition(), coordinateTransformer.getPosition(next));
            }
        }

        //TODO IMPLEMENT PICK UP TREASURE

        int nextPosition = movesThatNeedToBeMade.get(0);

        while(!checkIfValidMove(player, calculateNextMove(player.getPosition(), coordinateTransformer.getPosition(nextPosition)), fullMapController)){
            movesAlreadyMade.add(nextPosition);
            movesThatNeedToBeMade.remove(0);
            nextPosition = movesThatNeedToBeMade.get(0);
        }

        calculatePathFromPositionToGoal(playerCoordinate, nextPosition, player);
        gettingToAPosition = true;
        return calculateNextMove(player.getPosition(), coordinateTransformer.getPosition(movesToExecute.get(0)));

    }

    private void calculatePathFromPositionToGoal(int playerPosition, int goalPosition, Player player){
            //tell the computer schritt für schritt was er hier gerade machen sollte
        System.out.println("Calculate path from " + playerPosition + " to " + goalPosition);
        EMove nextMove = calculateNextMove(coordinateTransformer.getPosition(playerPosition), coordinateTransformer.getPosition(goalPosition));

        while(playerPosition != goalPosition){

            if(checkIfValidMove(player, nextMove, fullMapController)){
                playerPosition = positionAtNextMove(playerPosition,nextMove);
                movesToExecute.add(playerPosition);
            }

            else{
                 if(checkIfValidMove(player, EMove.Down, fullMapController)){
                     playerPosition = positionAtNextMove(playerPosition,nextMove);
                     movesToExecute.add(playerPosition);
                }
                else if(checkIfValidMove(player,EMove.Up, fullMapController)){
                     playerPosition = positionAtNextMove(playerPosition,nextMove);
                     movesToExecute.add(playerPosition);
                } else if (checkIfValidMove(player, EMove.Down, fullMapController)) {
                     playerPosition = positionAtNextMove(playerPosition,nextMove);
                     movesToExecute.add(playerPosition);
                 }
                else{
                     playerPosition = positionAtNextMove(playerPosition,nextMove);
                     movesToExecute.add(playerPosition);
                 }
            }
            nextMove = calculateNextMove(coordinateTransformer.getPosition(playerPosition), coordinateTransformer.getPosition(goalPosition));
        }
    }

    private EMove calculateNextMove(Point actualPosition, Point needToGoTo){
        //   String toPrint = "Actual position: " + actualPosition + " need to go: " + needToGoTo;
        if(needToGoTo.x > actualPosition.x){
            //     toPrint += " move: Right";
            // System.out.println(toPrint);
            return EMove.Right;
        } else if (needToGoTo.x < actualPosition.x) {
            //   toPrint += " move: Left";
            //  System.out.println(toPrint);
            return EMove.Left;
        } else if (needToGoTo.y < actualPosition.y) {
            //   toPrint += " move: UP";
            //   System.out.println(toPrint);
            return EMove.Up;
        } else {
            //     toPrint += " move: Down";
            //     System.out.println(toPrint);
            return EMove.Down;
        }
    }
    private int positionAtNextMove(int position, EMove move){
        Point p = coordinateTransformer.getPosition(position);
        switch (move){
            case Up -> {
                return coordinateTransformer.transformPlayerCoordinate(p.x, p.y-1);
            }
            case Down -> {
                return coordinateTransformer.transformPlayerCoordinate(p.x, p.y+1);
            }
            case Left -> {
                return coordinateTransformer.transformPlayerCoordinate(p.x-1, p.y);
            }
            case Right -> {
                return coordinateTransformer.transformPlayerCoordinate(p.x+1, p.y);
            }
        }
        return 0;
    }

    public void initializeGraph(int playerCoordinate, Player player){
        Graph graph = new Graph(playerCoordinate, true);
        movesThatNeedToBeMade = graph.getReturnMoves();
        System.out.println(movesThatNeedToBeMade + "\nPlayer position was: " + playerCoordinate + "(x:" + player.getPosition().x + " y:" + player.getPosition().y + ")");
        movesThatNeedToBeMade.remove(0);
        firstIteration = false;
    }

    private boolean checkIfValidMove(Player player, EMove move, FullMapController fullMapNodes){
            Point playerPosition = player.getPosition();
            switch (move){
                case Up -> {
                    return fullMapNodes.isFullMapNodeValid(new Point(playerPosition.x, playerPosition.y - 1));
                }
                case Down -> {
                    return fullMapNodes.isFullMapNodeValid(new Point(playerPosition.x, playerPosition.y + 1));
                }
                case Left -> {
                    return fullMapNodes.isFullMapNodeValid(new Point(playerPosition.x - 1, playerPosition.y));
                }
                case Right -> {
                    return fullMapNodes.isFullMapNodeValid(new Point(playerPosition.x + 1, playerPosition.y));
                }
            } return false;
    }



}
