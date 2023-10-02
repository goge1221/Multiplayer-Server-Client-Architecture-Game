package server.main.validator;

import messagesBase.UniqueGameIdentifier;
import server.main.game.GameHandler;
import server.main.mapHandler.MapNode;

import java.util.Collection;

public interface IRulesValidator {
    void validateMap(Collection<MapNode>halfMapNodes);
    void playerChecks(GameHandler gameHandler, UniqueGameIdentifier gameId, String playerId);
    void gameChecks(GameHandler gameHandler, UniqueGameIdentifier gameId);
}
