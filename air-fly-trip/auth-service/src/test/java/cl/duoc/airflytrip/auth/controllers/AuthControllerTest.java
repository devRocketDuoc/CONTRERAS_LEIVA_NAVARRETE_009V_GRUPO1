package cl.duoc.airflytrip.auth.controllers;

import cl.duoc.airflytrip.auth.dtos.request.CreateUserRequest;
import cl.duoc.airflytrip.auth.dtos.request.LoginRequest;
import cl.duoc.airflytrip.auth.dtos.request.RegisterRequest;
import cl.duoc.airflytrip.auth.dtos.response.AuthResponse;
import cl.duoc.airflytrip.auth.dtos.response.UserResponse;
import cl.duoc.airflytrip.auth.services.AuthService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  @Test
  void registerShouldReturnCreatedUser() {

    RegisterRequest request = RegisterRequest.builder()
        .email("user@example.com")
        .password("secret123")
        .firstName("Ana")
        .lastName("Perez")
        .build();

    UserResponse responseSimulado = userResponse(1L, "user@example.com");

    when(authService.register(any(RegisterRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<UserResponse> result = authController.register(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("user@example.com", result.getBody().getEmail());
  }

  @Test
  void loginShouldReturnToken() {

    LoginRequest request = LoginRequest.builder()
        .email("user@example.com")
        .password("secret123")
        .build();

    AuthResponse responseSimulado = AuthResponse.builder()
        .token("jwt-token")
        .tokenType("Bearer")
        .user(userResponse(1L, "user@example.com"))
        .build();

    when(authService.login(any(LoginRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<AuthResponse> result = authController.login(request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("jwt-token", result.getBody().getToken());
    assertEquals("Bearer", result.getBody().getTokenType());
  }

  @Test
  void meShouldResolveAuthenticatedPrincipal() {

    UserResponse responseSimulado = userResponse(2L, "me@example.com");

    @SuppressWarnings("all")
    UserDetails userDetailsMock = org.mockito.Mockito.mock(UserDetails.class);
    when(userDetailsMock.getUsername()).thenReturn("me@example.com");
    when(authService.getCurrentUser("me@example.com")).thenReturn(responseSimulado);

    ResponseEntity<UserResponse> result = authController.getCurrentUser(userDetailsMock);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("me@example.com", result.getBody().getEmail());
  }

  @Test
  void createUserShouldUseAuthenticatedPrincipal() {

    CreateUserRequest request = CreateUserRequest.builder()
        .email("new-user@example.com")
        .password("secret123")
        .firstName("New")
        .lastName("User")
        .role("CLIENT")
        .build();

    UserResponse responseSimulado = userResponse(3L, "new-user@example.com");

    @SuppressWarnings("all")
    UserDetails userDetailsMock = org.mockito.Mockito.mock(UserDetails.class);
    when(userDetailsMock.getUsername()).thenReturn("admin@example.com");
    when(authService.createUser(any(CreateUserRequest.class), eq("admin@example.com"))).thenReturn(responseSimulado);

    ResponseEntity<UserResponse> result = authController.createUser(request, userDetailsMock);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("new-user@example.com", result.getBody().getEmail());
  }

  private UserResponse userResponse(Long id, String email) {
    return UserResponse.builder()
        .id(id)
        .email(email)
        .firstName("Ana")
        .lastName("Perez")
        .documentNumber("12345678-9")
        .phone("+56 9 1234 5678")
        .role("CLIENT")
        .status("ACTIVE")
        .enabled(true)
        .createdAt(LocalDateTime.of(2026, 1, 1, 12, 0))
        .build();
  }
}