package client.main;

import client.main.network.ClientNetwork;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ControllerTest {

    @Test
    void GivenPlayersSendBothTheirHalfMap_ReceiveFullMap_ExpectedCallGetFullMap() {
        //Arrange
        ClientNetwork clientNetwork = Mockito.mock(ClientNetwork.class);
        Controller controller = new Controller(clientNetwork);

        //Act
        controller.updateMap();

        //Assert
        Mockito.verify(clientNetwork, Mockito.atLeastOnce()).getFullMap();

    }


}