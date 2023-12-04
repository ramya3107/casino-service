package ca.omega.casinoservice.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserBetSummaryDto {
  private Long userId; // 0
  private Long numberOfBets;
  private Long numberOfWins;
  private Double totalBetAmount;
  private Double totalWinAmount;
}
