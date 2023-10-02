package server.main;

import messagesBase.ResponseEnvelope;
import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.GameState;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.exceptions.GenericExampleException;
import server.main.game.GameHandler;
import server.main.player.EPlayerServerState;
import server.main.player.Player;
import server.main.validator.RulesValidator;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {

    private final GameHandler gameHandler = new GameHandler();
    private final RulesValidator rulesValidator = new RulesValidator();

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody UniqueGameIdentifier newGame() {
        UniqueGameIdentifier gameIdentifier = RandomGameIdGenerator.generateRandomGameId();
        gameHandler.addNewGame(gameIdentifier, rulesValidator);
        return gameIdentifier;
    }

    // example for a POST endpoint based on /games/{gameID}/players
    @RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
            @Validated @PathVariable UniqueGameIdentifier gameID,
            @Validated @RequestBody PlayerRegistration playerRegistration) {

        UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());

        Player newPlayer = new Player(playerRegistration.getStudentFirstName(), playerRegistration.getStudentLastName(),
                EPlayerServerState.MustWait, playerRegistration.getStudentUAccount(), newPlayerID);

        rulesValidator.performRegistrationPlayerChecks(gameHandler, gameID, newPlayerID.getUniquePlayerID());
        gameHandler.addPlayerToGame(gameID, newPlayer);

        return new ResponseEnvelope<>(newPlayerID);
    }

    @RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<GameState> receiveHalfMaps(
            @Validated @PathVariable UniqueGameIdentifier gameID,
            @Validated @RequestBody PlayerHalfMap playerHalfMap) {

        rulesValidator.performGameStateChecks(gameHandler, gameID, playerHalfMap.getUniquePlayerID());
        gameHandler.addMapNodes(gameID, playerHalfMap.getMapNodes(), playerHalfMap.getUniquePlayerID());

        return new ResponseEnvelope<>();
    }

    @RequestMapping(value = "/{gameID}/states/{playerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<GameState> sendGameState(
            @Validated @PathVariable UniqueGameIdentifier gameID,
            @Validated @PathVariable UniquePlayerIdentifier playerId) {

        rulesValidator.performGameStateChecks(gameHandler, gameID, playerId.getUniquePlayerID());
        return new ResponseEnvelope<>(gameHandler.getGameState(gameID, playerId));
    }


    @ExceptionHandler({GenericExampleException.class})
    public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
        ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        return result;
    }
}
