package ca.omega.casinoservice.processor;

import ca.omega.casinoservice.exceptions.InvalidBetInputException;
import ca.omega.casinoservice.models.Bet;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static ca.omega.casinoservice.domain.AppConstants.ROULETTE_MAX_NUMBER;

@Slf4j
public class RouletteNumberGameProcessor extends GameProcessor{
  @Override
  public void processGame(Bet bet) throws InvalidBetInputException {
    int userChoice;
    // Since this is a number game it should be number input.
    try {
      userChoice  = Integer.parseInt(bet.getBetOption());
    } catch (NumberFormatException e) {
      log.error("Selected option for this game is invalid");
      throw new InvalidBetInputException("Invalid option for this game");
    }
    Random random = new Random();
    int randomNumber = random.nextInt(ROULETTE_MAX_NUMBER);
    boolean gameResult = userChoice == randomNumber;
    String spinNumber = String.valueOf(randomNumber);
    addBetResultInfo(bet, spinNumber, gameResult);
  }
}
