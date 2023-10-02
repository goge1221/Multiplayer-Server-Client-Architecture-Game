package server.main.validator;

import messagesBase.UniqueGameIdentifier;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;
import server.main.validator.mapRules.*;

import java.util.Collection;
import java.util.HashSet;

public class RulesValidator {

    private final HashSet<IRulesValidator> gameRulesSet = new HashSet<>();
    private final HashSet<IRulesValidator> playerRegistrationRulesSet = new HashSet<>();
    private final HashSet<IRulesValidator> gameStateRulesSet = new HashSet<>();
    private final HashSet<IRulesValidator> mapValidatingRulesSet = new HashSet<>();


    public RulesValidator(){
        fillRulesSet();
    }

    private void performGameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId){
        for (IRulesValidator rule : gameRulesSet){
            rule.gameChecks(gameHandler, gameId);
        }
    }

    public void performRegistrationPlayerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId){
        performGameChecks(gameHandler, gameId);
        for (IRulesValidator rule : playerRegistrationRulesSet)
            rule.playerChecks(gameHandler, gameId, playerId);
    }

    public void performMapChecks(Collection<MapNode> halfMapNodes){
        for (IRulesValidator rule : mapValidatingRulesSet)
            rule.validateMap(halfMapNodes);
    }


    public void performGameStateChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId){
        performGameChecks(gameHandler, gameId);
        for (IRulesValidator rule : gameStateRulesSet)
            rule.playerChecks(gameHandler, gameId, playerId);
    }

    private void fillRulesSet(){
        gameRulesSet.add(new CheckIfGameIdValid());
        playerRegistrationRulesSet.add(new CheckPlayerCount());
        gameStateRulesSet.add(new CheckIfPlayerIsRegisteredToGame());
        fillMapValidatingRules();
    }

    private void fillMapValidatingRules(){
        mapValidatingRulesSet.add(new CheckIfCastlePlacedCorrectly());
        mapValidatingRulesSet.add(new CheckNumberOfCastles());
        mapValidatingRulesSet.add(new CheckWaterOnLongEdges());
        mapValidatingRulesSet.add(new CheckWaterOnShortEdges());
        mapValidatingRulesSet.add(new CountTerrainTypes());
    }

}
