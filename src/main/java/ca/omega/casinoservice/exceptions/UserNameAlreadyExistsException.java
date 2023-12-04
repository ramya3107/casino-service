package ca.omega.casinoservice.exceptions;

public class UserNameAlreadyExistsException extends Exception{

  public UserNameAlreadyExistsException(String message) {
    super(message);
  }
}
