package ca.omega.casinoservice.repository;

import ca.omega.casinoservice.models.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

  @Query(value = "select * from bet where user_id = ?1", nativeQuery = true)
  public List<Bet> getAllBetsByUser(long userId);
}
