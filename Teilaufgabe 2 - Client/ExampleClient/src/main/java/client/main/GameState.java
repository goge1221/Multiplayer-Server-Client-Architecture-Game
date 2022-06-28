package client.main;

import MessagesBase.MessagesFromServer.EPlayerGameState;
import MessagesBase.MessagesFromServer.PlayerState;
import MessagesBase.ResponseEnvelope;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GameState {

    private WebClient webClient;
    private String gameId;

    GameState(WebClient webClient, String gameId){
        this.webClient = webClient;
        this.gameId = gameId;
    }


    public EPlayerGameState getPlayerGameState(String playerId){

        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class); // specify the

        ResponseEnvelope<MessagesBase.MessagesFromServer.GameState> requestResult = webAccess.block();

        PlayerState playerState = (PlayerState) requestResult.getData().get().getPlayers().toArray()[0];
        return playerState.getState();
    }

    public void waitTillMustAct(String playerId) throws InterruptedException{
        EPlayerGameState ePlayerGameState = getPlayerGameState(playerId);
        while (ePlayerGameState != EPlayerGameState.MustAct) {
            if (ePlayerGameState == EPlayerGameState.Lost || ePlayerGameState == EPlayerGameState.Won) {
                return;
            }
            Thread.sleep(400);
            ePlayerGameState = getPlayerGameState(playerId);
        }
    }

}
