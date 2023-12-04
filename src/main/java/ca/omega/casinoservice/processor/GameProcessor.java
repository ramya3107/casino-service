package ca.omega.casinoservice.processor;

import ca.omega.casinoservice.exceptions.InvalidBetInputException;
import ca.omega.casinoservice.models.Bet;
import ca.omega.casinoservice.models.Game;
import ca.omega.casinoservice.models.User;

import java.math.BigDecimal;

public abstract class GameProcessor {

  public abstract void processGame(Bet bet) throws InvalidBetInputException;

  public void addBetResultInfo(Bet bet, String gameOutcome, boolean gameResult) {
    Game game = bet.getGame();
    User user = bet.getUser();
    bet.setOutcome(gameOutcome);
    bet.setResult(gameResult);
    BigDecimal winAmount;
    if (gameResult) {
      int winMultiplier = game.getWinMultiplier();
      winAmount = bet.getBetAmount().multiply(BigDecimal.valueOf(winMultiplier));
    } else {
      // This is used to calculate total gain or less on summary.
      winAmount = bet.getBetAmount().multiply(BigDecimal.valueOf(-1));
    }
    bet.setWinAmount(winAmount);
  }
}
