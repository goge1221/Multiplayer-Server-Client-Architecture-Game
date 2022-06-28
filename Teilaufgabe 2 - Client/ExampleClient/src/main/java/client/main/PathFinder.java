package client.main;

import MessagesBase.MessagesFromClient.EMove;
import MessagesBase.MessagesFromClient.HalfMapNode;
import MessagesBase.MessagesFromClient.PlayerMove;
import MessagesBase.MessagesFromServer.FullMapNode;
import client.main.FullMap.FullMapController;
import client.main.Player.Player;

import java.util.ArrayList;
import java.util.Collection;

public class PathFinder {

    private TreasureFinder treasureFinder = new TreasureFinder();

    public PlayerMove getNextMove(boolean horizontalMapForm, Player player, FullMapController fullMapController, Collection<HalfMapNode> internalHalfMap){
        if(!player.playerHasTreasure()){
            return PlayerMove.of(player.getPlayerId(), treasureFinder.getNextMove(player, fullMapController, internalHalfMap));
        }
        if(player.playerHasTreasure())System.out.println("\n---EURIKA PLAYER HAS TREASURE------\n");
        return PlayerMove.of(player.getPlayerId(), calculateMove(horizontalMapForm, player, fullMapController.getFullMapCollection()));
    }

    private EMove calculateMove(boolean horizontalMapForm, Player player, Collection<FullMapNode> fullMapCollection){


        ArrayList<FullMapNode>fullMapArray = new ArrayList<>(fullMapCollection);


        //map ist entweder 8x8 oder 4x16

        return EMove.Right;

    }

}
