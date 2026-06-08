package cl.duoc.airflytrip.auth.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.security.core.userdetails.UserDetails;

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
        UserResponse response = userResponse(1L, "user@example.com");

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        var result = authController.register(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("user@example.com");
        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void loginShouldReturnToken() {
        LoginRequest request = LoginRequest.builder()
                .email("user@example.com")
                .password("secret123")
                .build();
        AuthResponse response = AuthResponse.builder()
                .token("jwt-token")
                .tokenType("Bearer")
                .user(userResponse(1L, "user@example.com"))
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        var result = authController.login(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getToken()).isEqualTo("jwt-token");
        assertThat(result.getBody().getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void meShouldResolveAuthenticatedPrincipal() {
        UserResponse response = userResponse(2L, "me@example.com");
        UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("me@example.com");
        when(authService.getCurrentUser("me@example.com")).thenReturn(response);

        var result = authController.getCurrentUser(userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("me@example.com");
        verify(authService).getCurrentUser("me@example.com");
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
        UserResponse response = userResponse(3L, "new-user@example.com");
        UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin@example.com");
        when(authService.createUser(any(CreateUserRequest.class), eq("admin@example.com"))).thenReturn(response);

        var result = authController.createUser(request, userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("new-user@example.com");
        verify(authService).createUser(any(CreateUserRequest.class), eq("admin@example.com"));
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
