package client.main.Player;

import MessagesBase.MessagesFromClient.ERequestState;
import MessagesBase.MessagesFromClient.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PlayerController {
    private Player player;
    private PlayerView playerView;

    public PlayerController(){
        player = new Player("Andrei", "Goje", "andreig01");
        playerView = new PlayerView();
    }

    public void registerPlayer(WebClient webClient, String gameId){
        PlayerRegistration playerReg = new PlayerRegistration(player.getStudentFirstName(),
                player.getStudentLastName(), player.getUaccount());

        Mono<ResponseEnvelope> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
                .body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

        if (resultReg.getState() == ERequestState.Error) {
            playerView.printErrorWhileRegistering(player, gameId, resultReg.getExceptionMessage());
        } else {
            UniquePlayerIdentifier uniqueID = resultReg.getData().get();
            player.setPlayerId(uniqueID.getUniquePlayerID());
            playerView.printPlayerDetails(player, gameId);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerID(){
        return player.getPlayerId();
    }

}
