package client.main;

import MessagesBase.MessagesFromClient.ERequestState;
import MessagesBase.MessagesFromClient.HalfMapNode;
import MessagesBase.ResponseEnvelope;
import client.main.FullMap.FullMapController;
import client.main.PathFinder.PathFinder;
import client.main.Player.Player;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class MoveHandler {

    private PathFinder pathFinder;

    MoveHandler(){
        pathFinder = new PathFinder();
    }
    public void sendMove(boolean horizontalMapForm, Player player, FullMapController fullMapCollection, String gameId, WebClient webClient, Collection<HalfMapNode>halfMapNodes){

            Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
                    .body(BodyInserters.fromValue(pathFinder.getNextMove(horizontalMapForm, player, fullMapCollection, halfMapNodes))) // specify the data which is sent to the server
                    .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

            ResponseEnvelope<MessagesBase.MessagesFromServer.GameState> resultReg = webAccess.block();
            if (resultReg.getState() == ERequestState.Error) {
                System.err.println("Client while sending move, errormessage: " + resultReg.getExceptionMessage());
            } else {
                System.out.println("Player moved successfully.");
            }
    }
}
