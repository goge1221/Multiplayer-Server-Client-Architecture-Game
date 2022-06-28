package client.main;

import MessagesBase.MessagesFromClient.HalfMap;
import MessagesBase.MessagesFromClient.HalfMapNode;
import MessagesBase.MessagesFromServer.*;
import client.main.FullMap.FullMapController;
import client.main.Player.Player;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;

public class Network {

    private String gameId;
    private String serverBaseUrl;
    private WebClient webClient;

    private FullMapController fullMapController;
    private GameState gameState;
    private ServerMapExchangeHandler serverMapExchange;
    private MoveHandler moveHandler;

    Network(String gameId, String serverBaseUrl, WebClient webClient) {
        this.gameId = gameId;
        this.serverBaseUrl = serverBaseUrl;
        this.webClient = webClient;
        this.serverMapExchange = new ServerMapExchangeHandler();
        this.gameState = new GameState(webClient, gameId);
        this.moveHandler = new MoveHandler();
    }

    public void sendHalfMap(String playerId, HalfMap halfMap, WebClient webClient) throws Exception {
        gameState.waitTillMustAct(playerId);
        serverMapExchange.sendHalfMap(playerId, gameId, halfMap, webClient);
    }

    public void getFullMapFromServer(String playerId) throws InterruptedException {

      //  waitTillMustAct(playerId);
        fullMapController = new FullMapController(serverMapExchange.getFullMap(gameId,playerId,webClient));
        fullMapController.printFullMap();
    }

    public void actualizeFullMap(Player player, boolean horizontalMap){
        Collection<FullMapNode>temporaryFullMap = serverMapExchange.getFullMap(gameId, player.getPlayerId(), webClient);
        fullMapController.setFullMap(temporaryFullMap);
        ArrayList<FullMapNode>mapNodes = new ArrayList<>(temporaryFullMap);

        int x = 0;
        int y = 0;

        ArrayList<FullMapNode> arrayList = new ArrayList<>(temporaryFullMap);

        int xInLoop = 7;

        if(horizontalMap) xInLoop = 15;

        boolean playerPositionCheck = false;

        for(int i = 0; i < mapNodes.size(); ++i){

            if(x > xInLoop){
                x = 0;
                ++y;
            }

            for(int k = 0; k < mapNodes.size(); ++k){
                if(arrayList.get(k).getX() == x && arrayList.get(k).getY() == y){
                    FullMapNode currentMapNode = arrayList.get(k);
                    if(currentMapNode.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition){
                        playerPositionCheck = true;
                        player.setPosition(currentMapNode.getX(), currentMapNode.getY());
                    }
                }
            }
            ++x;
            if(playerPositionCheck) break;
        }

    }

    public EPlayerGameState getStatus(String playerId){
        return gameState.getPlayerGameState(playerId);
    }

    public void sendMove(Player player, Collection<HalfMapNode>halfMapNodes) throws InterruptedException {
        EPlayerGameState actualGameState = gameState.getPlayerGameState(player.getPlayerId());

        while (actualGameState != EPlayerGameState.Lost && actualGameState != EPlayerGameState.Won){
            gameState.waitTillMustAct(player.getPlayerId());
            actualizeFullMap(player, fullMapController.isMapHorizontal());
            fullMapController.printFullMap();
            moveHandler.sendMove(fullMapController.isMapHorizontal() ,player, fullMapController, gameId, webClient, halfMapNodes);
            actualGameState = gameState.getPlayerGameState(player.getPlayerId());
    } System.out.println("You have " + gameState.getPlayerGameState(player.getPlayerId()) + " the game.");
    }

    public void printFullMap(){
        fullMapController.printFullMap();
    }

}
