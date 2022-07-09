package client.main;

import client.main.HalfMap.HalfMapController;
import client.main.Player.PlayerController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class Converter {

    private String serverBaseUrl;
    private String gameId;
    private WebClient baseWebClient;
    private PlayerController playerController;

    private HalfMapController halfMapController;

    private Network network;

    Converter(String serverBaseUrl, String gameId){
        this.serverBaseUrl = serverBaseUrl;
        this.gameId = gameId;
        this.playerController = new PlayerController();
        this.halfMapController = new HalfMapController();
    }

    /*
     * This part of the code is used to create a new Game
     */
    public void createNewGame(){

        baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
                // XML
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

        System.out.println("New game was successfully created.");
        this.network = new Network(gameId, serverBaseUrl, baseWebClient);
    }

    /*
     * This part of the code handles the interactions between the converter and the player
     */
    public void registerPlayer(){
        playerController.registerPlayer(baseWebClient, gameId);
    }


    /*
     * This part of the code handles the interactions between the converter and the internally created HalfMap
     */
    public void createHalfMap(){
        //player.getplayerid()
        halfMapController.createHalfMap("playerid");
    }

    public void getHalfMapInfos(){ halfMapController.getViewUpdates();}

    public void sendHalfMap() throws Exception {
        network.sendHalfMap(playerController.getPlayerID(), halfMapController.getHalfMap(), baseWebClient);
    }

    public void getWholeMap() throws InterruptedException {
        network.getFullMapFromServer(playerController.getPlayerID());
    }

    public void sendMove() throws InterruptedException {
        System.out.println("\nENTERING MOVE SECTION\n");
        network.sendMove(playerController.getPlayer(), halfMapController.getHalfMap().getMapNodes());
    }
    public void getStatus(){
        System.out.println((network.getStatus(playerController.getPlayerID())));
    }


}
