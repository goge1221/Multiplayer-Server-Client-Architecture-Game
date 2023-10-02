package client.main.exceptions;

import client.main.network.ClientNetwork;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationExceptionTest {

    @Test
    void GivenClientTriesToRegister_ServerSendsBackError_ClientThrowsError() throws RegistrationException {

        //Assert
        ClientNetwork clientNetwork = Mockito.mock(ClientNetwork.class);

        //Act
        Mockito.doThrow(new RegistrationException("Registration exception"))
                .when(clientNetwork).registerPlayer();

        //Assert
        assertThrows(RegistrationException.class, clientNetwork::registerPlayer);

    }


}