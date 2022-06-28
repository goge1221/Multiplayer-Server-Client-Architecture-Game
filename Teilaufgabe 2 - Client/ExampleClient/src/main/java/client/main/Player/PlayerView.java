package client.main.Player;

public class PlayerView {
    public void printPlayerDetails(Player player, String gameId){
        System.out.println(player.getStudentFirstName() + " (id: " + player.getPlayerId() + ") successfully added to game "
        + gameId + ".");
    }

    public void printErrorWhileRegistering(Player player, String gameId, String errorMessage){
        System.err.println("Error while registering " + player.getUaccount() + " to game " + gameId + ":" + errorMessage);
    }

}
