package cl.duoc.airflytrip.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.duoc.airflytrip.auth.dtos.request.CreateUserRequest;
import cl.duoc.airflytrip.auth.dtos.request.LoginRequest;
import cl.duoc.airflytrip.auth.dtos.request.RegisterRequest;
import cl.duoc.airflytrip.auth.dtos.response.AuthResponse;
import cl.duoc.airflytrip.auth.dtos.response.UserResponse;
import cl.duoc.airflytrip.auth.exceptions.ConflictException;
import cl.duoc.airflytrip.auth.exceptions.ForbiddenException;
import cl.duoc.airflytrip.auth.exceptions.UnauthorizedException;
import cl.duoc.airflytrip.auth.models.AppUser;
import cl.duoc.airflytrip.auth.models.UserRole;
import cl.duoc.airflytrip.auth.models.UserStatus;
import cl.duoc.airflytrip.auth.repositories.AppUserRepository;
import cl.duoc.airflytrip.auth.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private AppUserRepository appUserRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthService authService;

  @Test
  void testRegistroExitoso() {

    RegisterRequest request = RegisterRequest.builder()
        .email("user@example.com")
        .password("secret123")
        .firstName("Ana")
        .lastName("Perez")
        .documentNumber("12.345.678-9")
        .phone("+56 9 1234 5678")
        .build();

    AppUser usuarioGuardado = AppUser.builder()
        .id(10L)
        .email("user@example.com")
        .passwordHash("encoded-secret")
        .firstName("Ana")
        .lastName("Perez")
        .role(UserRole.CLIENT)
        .status(UserStatus.ACTIVE)
        .enabled(true)
        .createdAt(LocalDateTime.of(2026, 1, 1, 12, 0))
        .build();

    when(appUserRepository.existsByEmail("user@example.com")).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
    when(appUserRepository.save(any(AppUser.class))).thenReturn(usuarioGuardado);
    UserResponse respuesta = authService.register(request);

    assertNotNull(respuesta);
    assertEquals(10L, respuesta.getId());
    assertEquals("user@example.com", respuesta.getEmail());
    assertEquals("CLIENT", respuesta.getRole());
    assertEquals("ACTIVE", respuesta.getStatus());
  }

  @Test
  void testRegistroFallaEmailDuplicado() {

    RegisterRequest request = RegisterRequest.builder()
        .email("user@example.com")
        .password("secret123")
        .firstName("Ana")
        .lastName("Perez")
        .build();

    when(appUserRepository.existsByEmail("user@example.com")).thenReturn(true);

    Exception exception = null;

    try {
      authService.register(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(ConflictException.class, exception.getClass());
    assertEquals("Email is already registered", exception.getMessage());
  }

  @Test
  void testLoginExitoso() {

    LoginRequest request = LoginRequest.builder()
        .email("admin@example.com")
        .password("secret123")
        .build();

    Authentication authenticationMock = org.mockito.Mockito.mock(Authentication.class);
    AppUser usuarioEnBD = sampleUser(20L, "admin@example.com", "Admin", "User", UserRole.ADMIN, UserStatus.ACTIVE,
        true);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authenticationMock);
    when(appUserRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(usuarioEnBD));
    when(jwtService.generateToken(usuarioEnBD)).thenReturn("token-falso-123");

    AuthResponse respuesta = authService.login(request);

    assertNotNull(respuesta);
    assertEquals("token-falso-123", respuesta.getToken());
    assertEquals("Bearer", respuesta.getTokenType());
    assertEquals("admin@example.com", respuesta.getUser().getEmail());
    assertEquals("ADMIN", respuesta.getUser().getRole());
  }

  @Test
  void testLoginFallaCredencialesInvalidas() {

    LoginRequest request = LoginRequest.builder()
        .email("user@example.com")
        .password("wrong-password")
        .build();

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("bad credentials"));

    Exception exception = null;
    try {
      authService.login(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(UnauthorizedException.class, exception.getClass());
    assertEquals("Invalid email or password", exception.getMessage());
  }

  @Test
  void testCrearUsuarioAdminCreaOperadorExitoso() {

    CreateUserRequest request = CreateUserRequest.builder()
        .email("  operator@Example.com ")
        .password("secret123")
        .firstName("  Camila ")
        .lastName("  Rojas ")
        .documentNumber(" 11.111.111-1 ")
        .phone(" +56 9 2222 2222 ")
        .role("operator")
        .build();

    AppUser adminCreador = sampleUser(1L, "admin@example.com", "Admin", "User", UserRole.ADMIN, UserStatus.ACTIVE,
        true);

    AppUser operadorGuardado = sampleUser(30L, "operator@example.com", "Camila", "Rojas", UserRole.OPERATOR,
        UserStatus.ACTIVE, true);
    operadorGuardado.setCreatedAt(LocalDateTime.of(2026, 1, 2, 9, 30));

    when(appUserRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminCreador));
    when(appUserRepository.existsByEmail("operator@example.com")).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
    when(appUserRepository.save(any(AppUser.class))).thenReturn(operadorGuardado);

    UserResponse response = authService.createUser(request, "ADMIN@Example.com ");

    assertNotNull(response);
    assertEquals(30L, response.getId());
    assertEquals("operator@example.com", response.getEmail());
    assertEquals("OPERATOR", response.getRole());
    assertEquals(LocalDateTime.of(2026, 1, 2, 9, 30), response.getCreatedAt());
  }

  @Test
  void testCrearUsuarioFallaOperadorNoPuedeCrearAdmin() {

    CreateUserRequest request = CreateUserRequest.builder()
        .email("admin2@example.com")
        .password("secret123")
        .firstName("Admin")
        .lastName("Two")
        .role("ADMIN")
        .build();

    AppUser operadorCreador = sampleUser(2L, "operator@example.com", "Operator", "User", UserRole.OPERATOR,
        UserStatus.ACTIVE, true);

    when(appUserRepository.findByEmail("operator@example.com")).thenReturn(Optional.of(operadorCreador));

    Exception exception = null;
    try {
      authService.createUser(request, "operator@example.com");
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(ForbiddenException.class, exception.getClass());
    assertEquals("You do not have permission to create a user with role ADMIN", exception.getMessage());
  }

  private AppUser sampleUser(
      Long id,
      String email,
      String firstName,
      String lastName,
      UserRole role,
      UserStatus status,
      Boolean enabled) {
    return AppUser.builder()
        .id(id)
        .email(email)
        .passwordHash("encoded")
        .firstName(firstName)
        .lastName(lastName)
        .role(role)
        .status(status)
        .enabled(enabled)
        .createdAt(LocalDateTime.of(2026, 1, 1, 8, 0))
        .build();
  }

}
