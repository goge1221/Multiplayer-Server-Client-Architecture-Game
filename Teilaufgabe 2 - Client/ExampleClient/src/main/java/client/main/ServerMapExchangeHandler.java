package client.main;

import MessagesBase.MessagesFromClient.ERequestState;
import MessagesBase.MessagesFromClient.HalfMap;
import MessagesBase.MessagesFromClient.HalfMapNode;
import MessagesBase.MessagesFromServer.FullMap;
import MessagesBase.MessagesFromServer.FullMapNode;
import MessagesBase.MessagesFromServer.GameState;
import MessagesBase.ResponseEnvelope;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

public class ServerMapExchangeHandler {

    public void sendHalfMap(String playerId,String gameId, HalfMap halfMap,WebClient webClient) throws Exception {

            Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
                    .body(BodyInserters.fromValue(halfMap)) // specify the data which is sent to the server
                    .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

            ResponseEnvelope<GameState> resultReg = webAccess.block();

            if (resultReg.getState() == ERequestState.Error) {
                System.err.println("Client while sending map, errormessage: " + resultReg.getExceptionMessage());
            } else {
                System.out.println("Map was sent successfully");
            }
    }

    public Collection<FullMapNode> getFullMap(String gameId, String playerId, WebClient baseWebClient){

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<GameState> requestResult = webAccess.block();

        return requestResult.getData().get().getMap().get().getMapNodes();
    }


}
