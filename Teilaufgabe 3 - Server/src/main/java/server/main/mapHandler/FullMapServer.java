package server.main.mapHandler;

import messagesBase.UniquePlayerIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.InvalidMapException;
import server.main.validator.RulesValidator;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class FullMapServer {
    private final Collection<MapNode> firstHalfMap = new HashSet<>();
    private final Collection<MapNode> secondHalfMap = new HashSet<>();
    private final Collection<MapNode> assembledMap = new HashSet<>();

    private final Logger logger = LoggerFactory.getLogger(FullMapServer.class);

    private Point firstPlayerPosition = new Point();
    private Point secondPlayerPosition = new Point();

    private Point firstPlayerCastlePosition = new Point();
    private Point secondPlayerCastlePosition = new Point();

    private final RulesValidator rulesValidator;

    public FullMapServer(RulesValidator rulesValidator) {
        this.rulesValidator = rulesValidator;
    }

    public Collection<MapNode> getMapNodes(UniquePlayerIdentifier playerId){
        if (playerId.getUniquePlayerID().equals(firstHalfMapPlayerId))
            return getMapWithEnemyCastleHidden(secondPlayerPosition, false);
        return getMapWithEnemyCastleHidden(firstPlayerPosition, true);
    }

    private String firstHalfMapPlayerId = "";
    private String secondHalfMapPlayerId = "";

    public void addMapNode(MapNode mapNode, boolean addToFirstMap){
        if (addToFirstMap) {
            firstHalfMap.add(mapNode);
        }
        else {
            secondHalfMap.add(mapNode);
        }
    }

    private Collection<MapNode> getMapWithEnemyCastleHidden(Point playerPosition, boolean hideFirstPlayer){
        Collection<MapNode> mapWithRandomEnemyPosition = new HashSet<>(hidePositionOfPlayer(playerPosition));
        logger.info(mapWithRandomEnemyPosition.toString());
        logger.info("Position of first castle " + firstPlayerCastlePosition + " second castle " + secondPlayerCastlePosition);
        if (hideFirstPlayer)
            return hideFirstPlayerCastle(mapWithRandomEnemyPosition);
        return hideSecondPlayerCastle(mapWithRandomEnemyPosition);
    }

    private Collection<MapNode> hideFirstPlayerCastle(Collection<MapNode> mapNodes){
        for (MapNode mapNode : mapNodes){
            if (mapNode.getyCoordinate() == firstPlayerCastlePosition.getY() && mapNode.getxCoordinate() == firstPlayerPosition.getX())
                mapNode.setFortStateServer(EFortStateServer.NoFortPresent);
            if (mapNode.getyCoordinate() == secondPlayerCastlePosition.getY() && mapNode.getxCoordinate() == secondPlayerCastlePosition.getX())
                mapNode.setFortStateServer(EFortStateServer.MyFortPresent);
        }
        return mapNodes;
    }

    private Collection<MapNode> hideSecondPlayerCastle(Collection<MapNode> mapNodes){
        for (MapNode mapNode : mapNodes){
            if (mapNode.getyCoordinate() == firstPlayerCastlePosition.getY() && mapNode.getxCoordinate() == firstPlayerPosition.getX())
                mapNode.setFortStateServer(EFortStateServer.MyFortPresent);
            if (mapNode.getyCoordinate() == secondPlayerCastlePosition.getY() && mapNode.getxCoordinate() == secondPlayerCastlePosition.getX())
                mapNode.setFortStateServer(EFortStateServer.NoFortPresent);
        }
        return mapNodes;
    }

    private Collection<MapNode> hidePositionOfPlayer(Point playerPosition){
        Collection<MapNode> mapNodesCopy = assembledMap;
        boolean enemyPlaced = false;
        boolean myPlayerPlaced = false;
        for (MapNode mapNode : mapNodesCopy){
            if (mapNode.getFortStateServer() == EFortStateServer.MyFortPresent && !myPlayerPlaced){
                mapNode.setPlayerState(EPlayerPositionServer.MyPlayerPresent);
                myPlayerPlaced = true;
            }
            if (mapNode.getxCoordinate() != playerPosition.getX() && !enemyPlaced) {
                mapNode.setPlayerState(EPlayerPositionServer.EnemyPlayerPresent);
                enemyPlaced = true;
            }
        }
        logger.debug("After hiding enemy player: " + mapNodesCopy);
        return mapNodesCopy;
    }


    private EMapForm getRandomMapForm(){
        if (Math.floor(Math.random()*2) % 2 == 0)
            return EMapForm.Horizontal;
        return EMapForm.Vertical;
    }

    public void setFirstPlayerId(String playerId){
        firstHalfMapPlayerId = playerId;
        for (MapNode mapNode : firstHalfMap)
            if (mapNode.getFortStateServer() == EFortStateServer.MyFortPresent) {
                firstPlayerPosition = new Point(mapNode.getxCoordinate(), mapNode.getyCoordinate());
                return;
            }
    }

    public void setSecondPlayerId(String playerId){
        secondHalfMapPlayerId = playerId;
        for (MapNode mapNode : secondHalfMap)
            if (mapNode.getFortStateServer() == EFortStateServer.MyFortPresent) {
                secondPlayerPosition = new Point(mapNode.getxCoordinate(), mapNode.getyCoordinate());
                return;
            }
    }

    public void assembleToFullMap(){
        Random random = new Random();
        boolean addFirstMapFirst = random.nextBoolean();

        EMapForm mapForm = getRandomMapForm();
        if (addFirstMapFirst) {
            assembleTheHalfMaps(firstHalfMap, secondHalfMap, mapForm);
            updatePlayerPosition(secondPlayerPosition, mapForm);
        }
        else {
            assembleTheHalfMaps(secondHalfMap, firstHalfMap, mapForm);
            updatePlayerPosition(firstPlayerPosition, mapForm);
        }
        firstPlayerCastlePosition = firstPlayerPosition;
        secondPlayerCastlePosition = secondPlayerPosition;
    }

    private void updatePlayerPosition(Point playerPosition, EMapForm mapForm){
        if (mapForm == EMapForm.Horizontal)
            playerPosition.x = (int) (playerPosition.getX() + 10);
        else
            playerPosition.y = (int) (playerPosition.getY() + 5);
    }

    private void assembleTheHalfMaps(Collection<MapNode> map1, Collection<MapNode> map2, EMapForm mapForm) {
        assembledMap.addAll(map1);

        for (MapNode mapNode : map2){
            if (mapForm == EMapForm.Horizontal)
                mapNode.setxCoordinate(mapNode.getxCoordinate() + 10);
            else
                mapNode.setyCoordinate(mapNode.getyCoordinate() + 5);
        }
        assembledMap.addAll(map2);
    }

    public void bothMapsOk() {
       rulesValidator.performMapChecks(firstHalfMap);
       rulesValidator.performMapChecks(secondHalfMap);
    }

    public String getCulprit() {

        try{
            rulesValidator.performMapChecks(firstHalfMap);
        } catch(InvalidMapException ignore){
            logger.info("Performed check of the half map of Player[" + firstHalfMapPlayerId + "] and was not OK");
            return firstHalfMapPlayerId;
        }

        try{
            rulesValidator.performMapChecks(secondHalfMap);
        } catch(InvalidMapException ignore){
            logger.info("Performed check of the half map of Player[" + secondHalfMapPlayerId + "] and was not OK");
            return secondHalfMapPlayerId;
        }

        return "Both maps ok!";
    }
}
