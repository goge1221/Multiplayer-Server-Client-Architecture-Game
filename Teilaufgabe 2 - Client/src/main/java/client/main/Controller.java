package client.main;

import client.main.exceptions.RegistrationException;
import client.main.map.fullMap.FullMapController;
import client.main.map.halfMap.HalfMapGenerator;
import client.main.map.mapNode.MapNode;
import client.main.moveHandler.MoveHandler;
import client.main.network.ClientNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


public class Controller {
    private final ClientNetwork clientNetwork;

    private final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final MoveHandler moveHandler = new MoveHandler();
    private final FullMapController fullMapController = new FullMapController();

    private Collection<MapNode> halfMapNodes;

    private int gameRound = 0;

    Controller(ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
    }

    Controller(String serverBaseUrl, String gameId){
        clientNetwork = new ClientNetwork(serverBaseUrl, gameId);
    }

    public void startGame(){
        registerPlayer();
        sendHalfMap();
        updateMap();
        goAndFindTreasure(halfMapNodes);
        transitionToEnemyMap();
        findEnemyCastle();
    }

    private void transitionToEnemyMap(){
        logger.debug("Transitioning now to enemy half map");
        while(!moveHandler.isPlayerOnEnemyField()){
            clientNetwork.sendMove(moveHandler.transitionToEnemyHalfMap(fullMapController.getFullMap(),
                    fullMapController.getPlayerPosition()));
            updateMap();
            System.out.println("\n---- Round " + gameRound + " ----");
            ++gameRound;
        }
        logger.info("The player reached the enemy HalfMap.");
    }

    private void findEnemyCastle(){
        logger.debug("Initiating search for enemy castle.");
        moveHandler.searchForEnemyCastle(fullMapController.getFullMap(), fullMapController.getPlayerPosition());
        int maxNumberOfMoves = 160;
        for (int i = 0; i < maxNumberOfMoves; ++i){
            moveHandler.checkIfPlayerIsOnMountain(fullMapController.getFullMap(), true);
            clientNetwork.sendMove(moveHandler.getNextMove(fullMapController.getPlayerPosition()));
            updateMap();
            System.out.println("\n---- Round " + gameRound + " ----");
            ++i;
            ++gameRound;
        }
    }

    private void goAndFindTreasure(Collection<MapNode> halfMapNodes){
        logger.debug("Initiating search for Treasure.");
        moveHandler.initializeTreasureFinder(halfMapNodes, fullMapController.getPlayerPosition());
        while(!clientNetwork.playerHasTreasure()){
             moveHandler.checkIfPlayerIsOnMountain(fullMapController.getFullMap(), false);
            clientNetwork.sendMove(moveHandler.getNextMove(fullMapController.getPlayerPosition()));
            updateMap();
            System.out.println("\n---- Round " + gameRound + " ----");
            ++gameRound;
        }
    }

    protected void registerPlayer(){
        try{
            clientNetwork.registerPlayer();
        } catch (RegistrationException registrationException){
            logger.error(registrationException.getMessage());
            System.exit(0);
        }
    }
    protected void sendHalfMap(){
        Collection<MapNode> mapNodeCollection = new HalfMapGenerator().getHalfMap();
        clientNetwork.sendHalfMap(mapNodeCollection);
        halfMapNodes = mapNodeCollection;
    }

    protected void updateMap(){
        fullMapController.updateFullMap(clientNetwork.getFullMap());
    }

}
