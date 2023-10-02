package client.main.network;

import client.main.exceptions.RegistrationException;
import client.main.map.mapNode.MapNode;
import client.main.moveHandler.EMoveClient;
import messagesBase.ResponseEnvelope;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.ERequestState;
import messagesBase.messagesFromServer.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class ClientNetwork {

    private final Logger logger = LoggerFactory.getLogger(ClientNetwork.class);
    private final WebClient webClient;
    private final String gameId;

    private boolean treasureCollected = false;
    private final Converter converter = new Converter();
    private String playerId;

    public ClientNetwork(String baseUrl, String gameId){
        webClient = WebClient.builder()
                .baseUrl(baseUrl + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
                // XML
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .build();

        this.gameId = gameId;
    }

    public void registerPlayer() throws RegistrationException {

        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
                .body(BodyInserters.fromValue(converter.toPlayerRegistration("Andrei", "Goje", "andreig01"))) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<UniquePlayerIdentifier> responseEnvelope = webAccess.block();

        if (responseEnvelope.getState() == ERequestState.Error)
            throw new RegistrationException(responseEnvelope.getExceptionName() + ": " + responseEnvelope.getExceptionMessage());

        playerId = converter.toStringPlayerId(responseEnvelope);
        logger.info("andreig01 registered successfully with id: " + playerId);
    }

    public boolean playerHasTreasure(){
        if (treasureCollected)
            logger.info("---- YOU FOUND THE TREASURE ----");
        return treasureCollected;
    }
    /*
    *********************************End**********************************
     */

    public void sendHalfMap(Collection<MapNode> halfMapNodes){

        try{
            waitTillMustAct();
        } catch (InterruptedException exception){
            logger.error(exception.getMessage());
            System.exit(1);
        }

        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
                .body(BodyInserters.fromValue(converter.toPlayerHalfMap(halfMapNodes, playerId)))
                .retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<GameState> regResponse = webAccess.block();
        if (regResponse.getState() == ERequestState.Error){
            logger.error(regResponse.getExceptionName() + ": " + regResponse.getExceptionMessage());
            System.exit(0);
        }
        logger.info("---- Half map was successfully sent! ----");
    }


    public Collection<MapNode> getFullMap(){
        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<GameState> requestResult = webAccess.block();
        return converter.toMapCollection(requestResult);
    }

    /*
    These 2 functions are responsible for making the player wait until it's
    his/her turn to make a move, or they inform you if the player lost or won
     */
    private void waitTillMustAct() throws InterruptedException {
        PlayerGameStateClient ePlayerGameState = getGameState();
        while (ePlayerGameState != PlayerGameStateClient.CanAct) {
            if (ePlayerGameState == PlayerGameStateClient.Lost || ePlayerGameState == PlayerGameStateClient.Won) {
                if (ePlayerGameState == PlayerGameStateClient.Lost)
                    logger.info("---- You lost the game! :( ----");
                if (ePlayerGameState == PlayerGameStateClient.Won)
                    logger.info("---- Congratulations! You won! :) ---- ");
                System.exit(0);
            }
            Thread.sleep(400);
            ePlayerGameState = getGameState();
        }
    }

    private PlayerGameStateClient getGameState(){
        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<GameState> requestResult = webAccess.block();

        treasureCollected = converter.toTreasureState(requestResult, playerId);

        return converter.toPlayerGameState(requestResult, playerId);
    }
    /*
    ************************END*****************************
     */


    public void sendMove(EMoveClient move){
        try{
            waitTillMustAct();
        } catch (InterruptedException e){
            logger.error("*** THREAD COULD NOT SLEEP ***");
        } finally{
            Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
                    .body(BodyInserters.fromValue(converter.toPlayerMove(playerId, move)))
                    .retrieve().bodyToMono(ResponseEnvelope.class);

            ResponseEnvelope<GameState> regResponse = webAccess.block();
            if (regResponse.getState() == ERequestState.Error){
                logger.error(regResponse.getExceptionName() + ": " + regResponse.getExceptionMessage());
                System.exit(1);
            }
        }
    }


}
