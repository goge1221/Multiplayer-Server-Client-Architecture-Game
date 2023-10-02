package server.main;

import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMapNode;
import messagesBase.messagesFromServer.*;
import server.main.mapHandler.EFortStateServer;
import server.main.mapHandler.EPlayerPositionServer;
import server.main.mapHandler.ETerrainServer;
import server.main.mapHandler.MapNode;
import server.main.player.EPlayerServerState;
import server.main.player.Player;

import java.util.Collection;
import java.util.HashSet;

public class Converter {

    public PlayerState toPlayerState(Player player, boolean generateRandomId) {

        UniquePlayerIdentifier uniquePlayerIdentifier = player.getUniquePlayerIdentifier();
        if (generateRandomId)
            uniquePlayerIdentifier = UniquePlayerIdentifier.random();

        return new PlayerState(
                player.getFirstName(),
                player.getLastName(),
                player.getuAccount(),
                toPlayerGameState(player.getServerState()),
                uniquePlayerIdentifier,
                false
        );
    }


    private EPlayerGameState toPlayerGameState(EPlayerServerState playerServerState) {
        switch (playerServerState) {
            case Won -> {
                return EPlayerGameState.Won;
            }
            case Lost -> {
                return EPlayerGameState.Lost;
            }
            case MustAct -> {
                return EPlayerGameState.MustAct;
            }
            default -> {
                return EPlayerGameState.MustWait;
            }
        }
    }

    public MapNode toMapNode(PlayerHalfMapNode mapNode) {

        EFortStateServer fortStateServer = EFortStateServer.NoFortPresent;
        if (mapNode.isFortPresent())
            fortStateServer = EFortStateServer.MyFortPresent;

        return new MapNode(
                fortStateServer,
                EPlayerPositionServer.NoPlayerPresent,
                toTerrainServer(mapNode.getTerrain()),
                false,
                mapNode.getX(),
                mapNode.getY()
        );
    }

    public FullMap toFullMap(Collection<MapNode> mapNodes) {
        Collection<FullMapNode> fullMapNodes = new HashSet<>();

        for (MapNode mapNode : mapNodes)
            fullMapNodes.add(toFullMapNode(mapNode));

        return new FullMap(fullMapNodes);
    }


    private FullMapNode toFullMapNode(MapNode mapNode) {
        return new FullMapNode(
                toTerrain(mapNode.getTerrainType()),
                toPlayerPosition(mapNode.getPlayerState()),
                toTreasureState(mapNode.isTreasurePresent()),
                toFortState(mapNode.getFortStateServer()),
                mapNode.getxCoordinate(),
                mapNode.getyCoordinate()
        );
    }

    private EPlayerPositionState toPlayerPosition(EPlayerPositionServer playerPositionServer) {
        switch (playerPositionServer) {
            case MyPlayerPresent -> {
                return EPlayerPositionState.MyPlayerPosition;
            }
            case BothPlayerPresent -> {
                return EPlayerPositionState.BothPlayerPosition;
            }
            case EnemyPlayerPresent -> {
                return EPlayerPositionState.EnemyPlayerPosition;
            }
            default -> {
                return EPlayerPositionState.NoPlayerPresent;
            }
        }
    }


    private ETerrain toTerrain(ETerrainServer terrainServer) {
        switch (terrainServer) {
            case Water -> {
                return ETerrain.Water;
            }
            case Mountain -> {
                return ETerrain.Mountain;
            }
            default -> {
                return ETerrain.Grass;
            }
        }
    }


    private ETreasureState toTreasureState(boolean treasurePresent) {
        if (treasurePresent)
            return ETreasureState.MyTreasureIsPresent;
        return ETreasureState.NoOrUnknownTreasureState;
    }

    private EFortState toFortState(EFortStateServer fortStateServer) {
        switch (fortStateServer) {
            case MyFortPresent -> {
                return EFortState.MyFortPresent;
            }
            case EnemyFortPresent -> {
                return EFortState.EnemyFortPresent;
            }
            default -> {
                return EFortState.NoOrUnknownFortState;
            }
        }
    }

    private ETerrainServer toTerrainServer(ETerrain terrain) {
        switch (terrain) {
            case Grass -> {
                return ETerrainServer.Grass;
            }
            case Water -> {
                return ETerrainServer.Water;
            }
            default -> {
                return ETerrainServer.Mountain;
            }
        }
    }

}
