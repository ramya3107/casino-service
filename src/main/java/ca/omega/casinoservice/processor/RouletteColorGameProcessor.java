package ca.omega.casinoservice.processor;

import ca.omega.casinoservice.exceptions.InvalidBetInputException;
import ca.omega.casinoservice.models.Bet;

import java.util.List;
import java.util.Random;

import static ca.omega.casinoservice.domain.AppConstants.BLACK_COLOR;
import static ca.omega.casinoservice.domain.AppConstants.RED_COLOR;

public class RouletteColorGameProcessor extends GameProcessor{

  List<String> colors = List.of(RED_COLOR, BLACK_COLOR);

  @Override
  public void processGame(Bet bet) throws InvalidBetInputException {
    String userChoice = bet.getBetOption();
    if (!(userChoice.equalsIgnoreCase(RED_COLOR) || userChoice.equalsIgnoreCase(BLACK_COLOR))) {
      throw new InvalidBetInputException("Color " + userChoice + "is invalid option");
    }
    String spinColor = getRandomColor();
    boolean gameResult = userChoice.equalsIgnoreCase(spinColor);
    addBetResultInfo(bet, spinColor, gameResult);
  }

  private String getRandomColor() {
    Random random = new Random();
    int randomPosition = random.nextInt(colors.size());
    return colors.get(randomPosition);
  }
}
