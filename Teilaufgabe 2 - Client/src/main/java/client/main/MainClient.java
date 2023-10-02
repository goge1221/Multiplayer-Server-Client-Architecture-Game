package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClient {
	

	public static void main(String[] args) {

		String serverBaseUrl = args[1];
		String gameId = args[2];

		Logger logger = LoggerFactory.getLogger(MainClient.class);

	//	String gameId = "2x0za";
	//	String serverBaseUrl = "http://swe1.wst.univie.ac.at";

		logger.info("\nGame with " + gameId + " started...");

		Controller controller = new Controller(serverBaseUrl, gameId);
		controller.startGame();

	}

}
