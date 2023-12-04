package ca.omega.casinoservice.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  @Column(unique = true)
  private String name;
  @NotNull
  private String description;
  @NotNull
  private Double winProbability;
  @NotNull
  private Integer winMultiplier;
  @NotNull
  private Double maxBetAmount;
  @NotNull
  private Double minBetAmount;
}
