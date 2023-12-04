package ca.omega.casinoservice.controller;

import ca.omega.casinoservice.exceptions.InvalidBetInputException;
import ca.omega.casinoservice.exceptions.ResourceNotFoundException;
import ca.omega.casinoservice.models.Bet;
import ca.omega.casinoservice.service.BetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ca.omega.casinoservice.domain.AppConstants.API_VERSION;

@RestController
@RequestMapping(API_VERSION)
public class BetController {

  private final BetService betService;

  public BetController(BetService betService) {
    this.betService = betService;
  }

  /**
   * To place bet against a game.
   * @param bet bet info with bet option and amount.
   * @param userId user who wants to bet.
   * @param gameId game on which user wants to place a bet.
   * @return bet details including result.
   * @throws ResourceNotFoundException when any resource not found (user, game).
   * @throws InvalidBetInputException if bet input is invalid. (amount restricts, low balance etc)
   */
  @PostMapping("/bet/{userId}/{gameId}")
  public ResponseEntity<Bet> placeBet(@RequestBody Bet bet, @PathVariable long userId, @PathVariable long gameId) throws ResourceNotFoundException, InvalidBetInputException {
    return new ResponseEntity<>(betService.placeBet(userId, gameId, bet), HttpStatus.CREATED);
  }

  /**
   * To get list of bets.
   * @return returns list of bets placed by all the players.
   */
  @GetMapping("/bets")
  public ResponseEntity<List<Bet>> getAllBets() {
    return new ResponseEntity<>(betService.getAllBetsInfo(), HttpStatus.OK);
  }
}
