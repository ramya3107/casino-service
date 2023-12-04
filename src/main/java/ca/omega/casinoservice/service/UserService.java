package ca.omega.casinoservice.service;

import ca.omega.casinoservice.dto.UserBetSummaryDto;
import ca.omega.casinoservice.exceptions.InvalidAmountException;
import ca.omega.casinoservice.exceptions.UserAgeException;
import ca.omega.casinoservice.exceptions.UserNameAlreadyExistsException;
import ca.omega.casinoservice.exceptions.ResourceNotFoundException;
import ca.omega.casinoservice.models.Bet;
import ca.omega.casinoservice.models.User;
import ca.omega.casinoservice.repository.BetRepository;
import ca.omega.casinoservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static ca.omega.casinoservice.domain.AppConstants.MIN_AGE_LIMIT;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BetRepository betRepository;

    public UserService(UserRepository userRepository, BetRepository betRepository) {
        this.userRepository = userRepository;
        this.betRepository = betRepository;
    }

    public User addUser(User user) throws UserNameAlreadyExistsException, UserAgeException {
        User result = getUserByUserName(user.getUserName());
        if (result != null) {
            log.error("User name :{} not available", user.getUserName());
            throw new UserNameAlreadyExistsException("Username already taken");
        }
        validateUserAge(user.getDateOfBirth());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserBetSummaryDto getBetSummary(long userId) throws ResourceNotFoundException {
        List<Bet> bets = betRepository.getAllBetsByUser(userId);
        if (bets.isEmpty()) {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new ResourceNotFoundException("User not found with given ID");
            }
        }
        return getUserBetSummary(bets, userId);
    }

    public User depositAmount(long userId, double amount) throws InvalidAmountException, ResourceNotFoundException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        Optional<User> result = userRepository.findById(userId);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("User not exists with given Id");
        }
        User user = result.get();
        user.setBalance(user.getBalance().add(BigDecimal.valueOf(amount)));
        userRepository.save(user);
        return user;
    }

    private void validateUserAge(LocalDate dateOfBirth) throws UserAgeException {
        long age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
        if (age < MIN_AGE_LIMIT) {
            log.error("User age must be equal or more than : {} to create account", MIN_AGE_LIMIT);
            throw new UserAgeException("Age must be greater than equal to " + MIN_AGE_LIMIT);
        }
    }

    private User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    private UserBetSummaryDto getUserBetSummary(List<Bet> bets, long userId) {
        UserBetSummaryDto dto = new UserBetSummaryDto();
        dto.setUserId(userId);
        dto.setNumberOfBets((long) bets.size());
        dto.setNumberOfWins(
            bets.stream()
            .filter(Bet::isResult)
            .count()
        );
        dto.setTotalBetAmount(
            bets.stream()
            .mapToDouble(b->b.getBetAmount().doubleValue()).sum()
        );
        dto.setTotalWinAmount(
            bets.stream()
            .mapToDouble(b->b.getWinAmount().doubleValue()).sum()
        );
        return dto;
    }
}
