package ca.omega.casinoservice.unit.service;


import ca.omega.casinoservice.exceptions.UserAgeException;
import ca.omega.casinoservice.exceptions.UserNameAlreadyExistsException;
import ca.omega.casinoservice.models.User;
import ca.omega.casinoservice.repository.BetRepository;
import ca.omega.casinoservice.repository.UserRepository;
import ca.omega.casinoservice.service.UserService;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BetRepository betRepository;

  @Test
  public void addUser_shouldIfUserNameIsUnique() throws UserAgeException, UserNameAlreadyExistsException {
    User mockUser = getMockUser();
    when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
    when(userRepository.save(mockUser)).thenReturn(mockUser);
    User result = userService.addUser(mockUser);
    Assertions.assertEquals(mockUser, result);
    verify(userRepository, times(1)).save(mockUser);
  }

  private User getMockUser() {
    User user = new User();
    user.setUserName("Ramya123");
    user.setName("Ramya");
    user.setDateOfBirth(LocalDate.now().minusYears(19));
    return user;
  }
}
