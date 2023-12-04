package ca.omega.casinoservice.controller;

import ca.omega.casinoservice.models.Game;
import ca.omega.casinoservice.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static ca.omega.casinoservice.domain.AppConstants.API_VERSION;

@RestController
@RequestMapping(API_VERSION)
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  /**
   * To Save games to system.
   * @param games list of games with info.
   * @return saved games.
   */
  @PostMapping("/game/data")
  public ResponseEntity<List<Game>> saveGames(@RequestBody List<Game> games) {
    return new ResponseEntity<>(gameService.saveGames(games), HttpStatus.CREATED);
  }

  /**
   * To save single game.
   * @param game game info.
   * @return saved game.
   */
  @PostMapping("/game")
  public ResponseEntity<Game> saveGame(@Valid @RequestBody Game game) {
    return new ResponseEntity<>(gameService.saveGame(game), HttpStatus.CREATED);
  }

  /**
   * To get list of available games.
   * @return available games info.
   */
  @GetMapping("/games")
  public ResponseEntity<List<Game>> getAllGames() {
    return new ResponseEntity<>(gameService.getAllGames(), HttpStatus.OK);
  }
}
