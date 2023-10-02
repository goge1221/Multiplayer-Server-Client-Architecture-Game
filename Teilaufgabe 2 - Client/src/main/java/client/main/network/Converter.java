package client.main.network;

import client.main.map.mapNode.EPlayerState;
import client.main.map.mapNode.ETerrainType;
import client.main.map.mapNode.MapNode;
import client.main.moveHandler.EMoveClient;
import messagesBase.ResponseEnvelope;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.*;
import messagesBase.messagesFromServer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

class Converter {

    private final Logger logger = LoggerFactory.getLogger(Converter.class);
    /*
    These 2 functions are related to registering the player and receiving the player id in form of a string
     */
    public PlayerRegistration toPlayerRegistration(String firstName, String lastName, String uAccount) {
        return new PlayerRegistration(firstName, lastName, uAccount);
    }

    public String toStringPlayerId(ResponseEnvelope<UniquePlayerIdentifier> registrationResponse) {
        if (registrationResponse.getData().isPresent())
            return registrationResponse.getData().get().getUniquePlayerID();
        return "Error Player id was not available";
    }

    //-----------------------------END-----------------------------------------


    /*
    This section is related to sending the player half map
     */
    public PlayerHalfMap toPlayerHalfMap(Collection<MapNode> mapNodeCollection, String playerId) {
        Collection<PlayerHalfMapNode> convertedMapNodes = new ArrayList<>();

        for (MapNode mapNode : mapNodeCollection)
            convertedMapNodes.add(toPlayerHalfMapNode(mapNode));

        return new PlayerHalfMap(playerId, convertedMapNodes);
    }


    private PlayerHalfMapNode toPlayerHalfMapNode(MapNode mapNode) {
        if (mapNode.isCastlePresent())
            return new PlayerHalfMapNode(mapNode.getCoordinates().x, mapNode.getCoordinates().y, true, toServerTerrainType(mapNode.getTerrainType()));
        return new PlayerHalfMapNode(mapNode.getCoordinates().x, mapNode.getCoordinates().y, toServerTerrainType(mapNode.getTerrainType()));
    }

    private ETerrain toServerTerrainType(ETerrainType terrainType) {
        switch (terrainType) {
            case WATER -> {
                return ETerrain.Water;
            }
            case MOUNTAIN -> {
                return ETerrain.Mountain;
            }
            default -> {
                return ETerrain.Grass;
            }
        }
    }
    // ----------------------------------END-------------------------------------


    /*
    These functions are needed for when asking for the game state and the treasure state of a player
     */

    public boolean toTreasureState(ResponseEnvelope<GameState> requestResult, String playerId) {
        if (requestResult.getData().isPresent())
            if (Objects.equals(((PlayerState) requestResult.getData().get().getPlayers().toArray()[0]).getUniquePlayerID(), playerId))
                return ((PlayerState) requestResult.getData().get().getPlayers().toArray()[0]).hasCollectedTreasure();

        return ((PlayerState) requestResult.getData().get().getPlayers().toArray()[1]).hasCollectedTreasure();
    }

    public PlayerGameStateClient toPlayerGameState(ResponseEnvelope<GameState> requestResult, String playerId) {
        if (requestResult.getData().isPresent())
            if (Objects.equals(((PlayerState) requestResult.getData().get().getPlayers().toArray()[0]).getUniquePlayerID(), playerId))
                return toGameStateClient(((PlayerState) requestResult.getData().get().getPlayers().toArray()[0]).getState());
        return toGameStateClient(((PlayerState) requestResult.getData().get().getPlayers().toArray()[1]).getState());
    }

    private PlayerGameStateClient toGameStateClient(EPlayerGameState playerGameState) {
        switch (playerGameState) {
            case Won -> {
                return PlayerGameStateClient.Won;
            }
            case Lost -> {
                return PlayerGameStateClient.Lost;
            }
            case MustAct -> {
                return PlayerGameStateClient.CanAct;
            }
            default -> {
                return PlayerGameStateClient.MustWait;
            }
        }
    }

    //-----------------------------------END----------------------------------


    /*
    These functions are needed in order to process the received FullMap from the server
     */

    public Collection<MapNode> toMapCollection(ResponseEnvelope<GameState> responseEnvelope) {
        checkIfResponseContainsError(responseEnvelope);
        if (responseEnvelope.getData().isPresent() && responseEnvelope.getData().get().getMap().isPresent())
            return toInternalMap(responseEnvelope.getData().get().getMap().get());
        return new ArrayList<>();
    }

    private Collection<MapNode> toInternalMap(FullMap fullMap) {
        Collection<MapNode> mapNodes = new HashSet<>();
        for (FullMapNode mapNode : fullMap.getMapNodes())
            mapNodes.add(toMapNode(mapNode));
        return mapNodes;
    }

    private MapNode toMapNode(FullMapNode fullMapNode) {
        return new MapNode(
                new Point(fullMapNode.getX(), fullMapNode.getY()),
                toClientTerrainType(fullMapNode.getTerrain()),
                toPlayerState(fullMapNode.getPlayerPositionState()),
                fullMapNode.getTreasureState() == ETreasureState.MyTreasureIsPresent,
                fullMapNode.getFortState() != EFortState.NoOrUnknownFortState);
    }

    private ETerrainType toClientTerrainType(ETerrain terrain) {
        switch (terrain) {
            case Water -> {
                return ETerrainType.WATER;
            }
            case Mountain -> {
                return ETerrainType.MOUNTAIN;
            }
            default -> {
                return ETerrainType.GRASS;
            }
        }
    }

    private EPlayerState toPlayerState(EPlayerPositionState playerPositionState) {
        switch (playerPositionState) {
            case MyPlayerPosition -> {
                return EPlayerState.MyPlayerPresent;
            }
            case EnemyPlayerPosition -> {
                return EPlayerState.EnemyPlayerPresent;
            }
            case BothPlayerPosition -> {
                return EPlayerState.BothPlayerPresent;
            }
            default -> {
                return EPlayerState.NoPlayerPresent;
            }
        }
    }

    //--------------------------------------END---------------------------------------


    /*
    These functions are needed for the move handling
     */

    public PlayerMove toPlayerMove(String playerId, EMoveClient clientMove) {
        return PlayerMove.of(playerId, toEMove(clientMove));
    }

    private EMove toEMove(EMoveClient clientMove) {
        switch (clientMove) {
            case UP -> {
                return EMove.Up;
            }
            case LEFT -> {
                return EMove.Left;
            }
            case DOWN -> {
                return EMove.Down;
            }
            default -> {
                return EMove.Right;
            }
        }
    }

    //--------------------------------------END------------------------------------


    private void checkIfResponseContainsError(ResponseEnvelope<?> registrationResponse) {
        if (registrationResponse.getState() == ERequestState.Error) {
            logger.error(registrationResponse.getExceptionName() + ": " + registrationResponse.getExceptionMessage());
            System.exit(1);
        }
    }

}
