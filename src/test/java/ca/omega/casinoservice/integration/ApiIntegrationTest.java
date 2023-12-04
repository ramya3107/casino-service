package ca.omega.casinoservice.integration;

import ca.omega.casinoservice.CasinoServiceApplication;
import ca.omega.casinoservice.models.Bet;
import ca.omega.casinoservice.models.Game;
import ca.omega.casinoservice.models.User;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CasinoServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiIntegrationTest {
  @LocalServerPort
  private int port;

  private static final String API_VERSION = "/api/v1";

  private Long testUserId;
  private Long testDepositUserId;
  private final Map<String, Long> gameIds = new HashMap<>();
  TestRestTemplate restTemplate = new TestRestTemplate();
  private static final String R_COLOR_GAME = "Roulette-Color";
  private static final String R_NUMBER_GAME = "Roulette-number";

  HttpHeaders headers = new HttpHeaders();

  @BeforeAll
  public void setUp() {
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    // add 1 user.
    User user = new User();
    user.setUserName("Ramya111");
    user.setName("Ramya");
    user.setDateOfBirth(LocalDate.now().minusYears(19));
    HttpEntity<User> entity = new HttpEntity<>(user, headers);
    ResponseEntity<User> response = restTemplate.exchange(
        createURLWithPort("/user/register"),
        HttpMethod.POST, entity, User.class);
    testUserId = Objects.requireNonNull(response.getBody()).getId();

    // add deposit user
    User depositUser = new User();
    depositUser.setUserName("Ramya222");
    depositUser.setName("Ramya");
    depositUser.setDateOfBirth(LocalDate.now().minusYears(19));
    HttpEntity<User> httpEntity = new HttpEntity<>(depositUser, headers);
    ResponseEntity<User> userResponse = restTemplate.exchange(
        createURLWithPort("/user/register"),
        HttpMethod.POST, httpEntity, User.class);
    testDepositUserId = Objects.requireNonNull(userResponse.getBody()).getId();

    // add games data

    Game colorGame = new Game();
    colorGame.setName(R_COLOR_GAME);
    colorGame.setDescription("In this game you have two options to bet as RED or BLACK.");
    colorGame.setWinProbability(0.5);
    colorGame.setWinMultiplier(1);
    colorGame.setMinBetAmount(10.0);
    colorGame.setMaxBetAmount(100.0);

    Game numberGame = new Game();
    numberGame.setName(R_NUMBER_GAME);
    numberGame.setDescription("In this game you have two options to bet on one number from 0 to 36. Winner will gets 35 times on bet amount");
    numberGame.setWinProbability(0.027);
    numberGame.setWinMultiplier(35);
    numberGame.setMinBetAmount(10.0);
    numberGame.setMaxBetAmount(50.0);

    List<Game> games = List.of(colorGame, numberGame);
    HttpEntity<List<Game>> gameData = new HttpEntity<>(games, headers);
    ResponseEntity<List<Game>> gameResponse = restTemplate.exchange(createURLWithPort("/game/data"),
        HttpMethod.POST, gameData, new ParameterizedTypeReference<>() {});
    gameResponse.getBody().forEach(g->
        gameIds.put(g.getName(), g.getId()));
  }

  @Test
  public void testAddUser() throws JSONException {
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    User user = new User();
    user.setUserName("Ramya123");
    user.setName("Ramya");
    user.setDateOfBirth(LocalDate.now().minusYears(19));
    HttpEntity<User> entity = new HttpEntity<>(user, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/user/register"),
        HttpMethod.POST, entity, String.class);
    String expectedResponse = "\"name\":\"Ramya\",\"userName\":\"Ramya123\"";
    Assertions.assertNotNull(response.getBody());
    Assertions.assertTrue(response.getBody().contains(expectedResponse));
    Assertions.assertEquals(201, response.getStatusCodeValue());

  }

  @Test
  public void testGetAllUsers() {
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/users"),
        HttpMethod.GET, entity, String.class);
    String expectedResponse = "\"name\":\"Ramya\",\"userName\":\"Ramya111\"";
    Assertions.assertNotNull(response.getBody());
    Assertions.assertTrue(response.getBody().contains(expectedResponse));
    Assertions.assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDepositAmount() {
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    HttpEntity<User> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        createURLWithPort("/user/"+ testDepositUserId +"/deposit/100.00"),
        HttpMethod.PUT, entity, String.class);
    System.out.println(response.getBody());
    String expectedResponse = "\"balance\":200.00";
    Assertions.assertNotNull(response.getBody());
    Assertions.assertTrue(response.getBody().contains(expectedResponse));
    Assertions.assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testAddGameData() {
      Game dummyGame = new Game();
      dummyGame.setName("Dummy-Game");
      dummyGame.setDescription("dummy-description");
      dummyGame.setWinProbability(0.23);
      dummyGame.setWinMultiplier(5);
      dummyGame.setMinBetAmount(5.00);
      dummyGame.setMaxBetAmount(50.00);

    List<Game> games
        = List.of(dummyGame);
    HttpEntity<List<Game>> gameData = new HttpEntity<>(games, headers);
    ResponseEntity<List<Game>> gameResponse = restTemplate.exchange(createURLWithPort("/game/data"),
        HttpMethod.POST, gameData, new ParameterizedTypeReference<>() {});
    Assertions.assertEquals(201, gameResponse.getStatusCodeValue());
    Assertions.assertNotNull(gameResponse.getBody());
    Assertions.assertEquals(1, gameResponse.getBody().size());
  }

  @Test
  public void testPlaceBetApi() {
    Bet bet = new Bet();
    bet.setBetAmount(BigDecimal.valueOf(20.00));
    bet.setBetOption("RED");
    Long gameId = gameIds.get(R_COLOR_GAME);
    HttpEntity<Bet> betData = new HttpEntity<>(bet, headers);
    ResponseEntity<Bet> response = restTemplate.exchange(
        createURLWithPort("/bet/" + gameId + "/" + testUserId),
        HttpMethod.POST, betData, Bet.class);
    Assertions.assertEquals(201, response.getStatusCodeValue());
    Assertions.assertNotNull(response.getBody());
    Bet result = response.getBody();
    Assertions.assertNotNull(result.getId());
    Assertions.assertNotNull(result.getOutcome());
    System.out.println(result);
    Assertions.assertNotEquals(0.0, result.getWinAmount().doubleValue());
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + API_VERSION + uri;
  }
}
