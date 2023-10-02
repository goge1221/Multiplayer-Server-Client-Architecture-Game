package server.main.player;

import messagesBase.UniquePlayerIdentifier;

public class Player {

    public EPlayerServerState getServerState() {
        return serverState;
    }

    public UniquePlayerIdentifier getUniquePlayerIdentifier() {
        return uniquePlayerIdentifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPlayerState(EPlayerServerState ePlayerServerState){
        this.serverState = ePlayerServerState;
    }

    public String getuAccount() {
        return uAccount;
    }

    private final String firstName;
    private final String lastName;
    private EPlayerServerState serverState;
    private final String uAccount;
    private final UniquePlayerIdentifier uniquePlayerIdentifier;

    public Player(String firstName, String lastName, EPlayerServerState serverState, String uAccount, UniquePlayerIdentifier uniquePlayerIdentifier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.serverState = serverState;
        this.uAccount = uAccount;
        this.uniquePlayerIdentifier = uniquePlayerIdentifier;
    }

    @Override
    public String toString() {
        return "{PlayerId: [" + uniquePlayerIdentifier + "], State: [" + serverState + "]}";
    }
}
