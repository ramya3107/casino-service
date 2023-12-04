package ca.omega.casinoservice.service;

import ca.omega.casinoservice.exceptions.InvalidBetInputException;
import ca.omega.casinoservice.exceptions.ResourceNotFoundException;
import ca.omega.casinoservice.models.Bet;
import ca.omega.casinoservice.models.Game;
import ca.omega.casinoservice.models.User;
import ca.omega.casinoservice.processor.GameProcessor;
import ca.omega.casinoservice.processor.RouletteColorGameProcessor;
import ca.omega.casinoservice.processor.RouletteNumberGameProcessor;
import ca.omega.casinoservice.repository.BetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static ca.omega.casinoservice.domain.AppConstants.ROULETTE_COLOR;
import static ca.omega.casinoservice.domain.AppConstants.ROULETTE_NUMBER;

@Service
public class BetService {

  private final BetRepository betRepository;
  private final UserService userService;
  private final GameService gameService;

  public BetService(BetRepository betRepository, UserService userService, GameService gameService) {
    this.betRepository = betRepository;
    this.userService = userService;
    this.gameService = gameService;
  }

  public Bet placeBet(long userId, long gameId, Bet bet) throws ResourceNotFoundException, InvalidBetInputException {
    // check if userId is valid
    User user = userService.getUser(userId);
    if (user == null) {
      throw new ResourceNotFoundException("Invalid User ID");
    }
    // check if gameId is valid
    Game game = gameService.getGame(gameId);
    if (game == null) {
      throw new ResourceNotFoundException("Invalid Game ID");
    }
    // validate Bet
    if (bet.getBetAmount().doubleValue() < game.getMinBetAmount()
        || bet.getBetAmount().doubleValue() > game.getMaxBetAmount()) {
      throw new InvalidBetInputException("Bet Amount should be between " + game.getMinBetAmount() + " to " +
          game.getMaxBetAmount() + "for " + game.getName());
    }
    // validate user balance
    if (bet.getBetAmount().compareTo(user.getBalance()) > 0) {
      throw new InvalidBetInputException("Insufficient balance for given user");
    }
    return processBet(game, user, bet);
  }

  public List<Bet> getAllBetsInfo() {
    return betRepository.findAll();
  }

  private Bet processBet(Game game, User user, Bet bet) throws InvalidBetInputException, ResourceNotFoundException {
    GameProcessor processor = null;
    if (game.getName().equalsIgnoreCase(ROULETTE_COLOR)) {
      processor = new RouletteColorGameProcessor();
    } else if (game.getName().equalsIgnoreCase(ROULETTE_NUMBER)) {
      processor = new RouletteNumberGameProcessor();
    } else {
      // Technically we don't reach to this, since we already validated game.
      throw new ResourceNotFoundException("Game Name Not Found");
    }
    bet.setUser(user);
    bet.setGame(game);
    processor.processGame(bet);
    updateUserBalance(bet);
    return betRepository.save(bet);
  }

  private void updateUserBalance(Bet bet) {
    BigDecimal updatedAmount;
    if (bet.isResult()) {
      updatedAmount = bet.getWinAmount();
    } else {
      updatedAmount = bet.getBetAmount().negate();
    }
    updatedAmount = bet.getUser().getBalance().add(updatedAmount);
    bet.getUser().setBalance(updatedAmount);
  }
}
