package client.main;

public class MainClient {


	public static void main(String[] args) throws Exception {

		String serverBaseUrl = "http://swe1.wst.univie.ac.at";
		String gameId = "QrbqF";

		//The converter is the class which acts as the interface between the MainClient and the Server
	/*
		//String serverBaseUrl = args[1];
		//String gameId = args[2];
*/
//		System.out.println("PLAYER 1");
		Converter converter = new Converter(serverBaseUrl, gameId);
	//	converter.createNewGame();
//		converter.registerPlayer();
		converter.createHalfMap();
		converter.getHalfMapInfos();
		//converter.sendHalfMap();

		//converter.getWholeMap();
		//converter.getStatus();

	//	converter.sendMove();

	//	converter.getRequest(serverBaseUrl, gameId, player2.getPlayerId(), internalHalfMap2.getHalfMap());
//		System.out.println("Player 2 sent map");

	}

}

