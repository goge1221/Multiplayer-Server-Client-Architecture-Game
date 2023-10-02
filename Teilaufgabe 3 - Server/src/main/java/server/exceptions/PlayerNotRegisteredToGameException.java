package server.exceptions;

public class PlayerNotRegisteredToGameException extends GenericExampleException{
    public PlayerNotRegisteredToGameException(String errorName, String errorMessage) {
        super(errorName, errorMessage);
    }
}
