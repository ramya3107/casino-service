package ca.omega.casinoservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

  ExceptionResponse exceptionResponse = new ExceptionResponse();

  @ExceptionHandler(UserNameAlreadyExistsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ExceptionResponse handleUsernameAlreadyExistsException(UserNameAlreadyExistsException e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

  @ExceptionHandler(UserAgeException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ExceptionResponse handleUserAgeException(UserAgeException e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

  @ExceptionHandler(InvalidAmountException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ExceptionResponse handleInvalidAmountException(InvalidAmountException e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody ExceptionResponse handleUserNotFoundException(ResourceNotFoundException e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

  @ExceptionHandler(InvalidBetInputException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ExceptionResponse handleInvalidBetInputException(InvalidBetInputException e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ExceptionResponse handleException(Exception e) {
    exceptionResponse.setErrorMessage(e.getMessage());
    return exceptionResponse;
  }

}
