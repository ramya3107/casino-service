package ca.omega.casinoservice.controller;

import ca.omega.casinoservice.dto.UserBetSummaryDto;
import ca.omega.casinoservice.exceptions.InvalidAmountException;
import ca.omega.casinoservice.exceptions.UserAgeException;
import ca.omega.casinoservice.exceptions.UserNameAlreadyExistsException;
import ca.omega.casinoservice.exceptions.ResourceNotFoundException;
import ca.omega.casinoservice.models.User;
import ca.omega.casinoservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ca.omega.casinoservice.domain.AppConstants.API_VERSION;


@RestController
@RequestMapping(API_VERSION)
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * To register a user.
   * @param user user info.
   * @return registered or saved user info.
   * @throws UserNameAlreadyExistsException if username exists already.
   * @throws UserAgeException if user age less than limit.
   */
  @PostMapping("/user/register")
  public ResponseEntity<User> addUser(@RequestBody User user) throws UserNameAlreadyExistsException, UserAgeException {
    return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
  }

  /**
   * To get list of users in system.
   * @return list of users.
   */
  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  }

  /**
   * To deposit amount to user.
   * @param amount amount to be added.
   * @param userId userId for which amount to be added.
   * @return updated user info with balance.
   * @throws InvalidAmountException if amount is not valid.
   * @throws ResourceNotFoundException if user not found in system.
   */
  @PutMapping("/user/{userId}/deposit/{amount}")
  public ResponseEntity<User> depositAmount(@PathVariable double amount, @PathVariable long userId) throws InvalidAmountException, ResourceNotFoundException {
    return new ResponseEntity<>(userService.depositAmount(userId, amount), HttpStatus.OK);
  }

  /**
   * To get bet summary for user.
   * @param userId user for which summary info requested.
   * @return user bet summary info.
   * @throws ResourceNotFoundException if user not found.
   */
  @GetMapping("/user/{userId}/bet")
  public ResponseEntity<UserBetSummaryDto> getBetSummary(@PathVariable long userId) throws ResourceNotFoundException {
    return new ResponseEntity<>(userService.getBetSummary(userId), HttpStatus.OK);
  }
    
}
