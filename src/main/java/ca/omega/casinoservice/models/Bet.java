package ca.omega.casinoservice.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
public class Bet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal betAmount;
  private String betOption;
  private String outcome;
  @ManyToOne
  private Game game;
  @ManyToOne
  private User user;
  private boolean result;
  private BigDecimal winAmount;
  @CreationTimestamp
  private OffsetDateTime createdAt;

}
