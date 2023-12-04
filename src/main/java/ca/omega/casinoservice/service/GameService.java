package ca.omega.casinoservice.service;

import ca.omega.casinoservice.models.Game;
import ca.omega.casinoservice.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

  private final GameRepository gameRepository;

  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public List<Game> saveGames(List<Game> games) {
    return gameRepository.saveAll(games);
  }

  public Game saveGame(Game game) {
    return gameRepository.save(game);
  }

  public List<Game> getAllGames() {
    return gameRepository.findAll();
  }

  public Game getGame(long id) {
    return gameRepository.findById(id).orElse(null);
  }
}
