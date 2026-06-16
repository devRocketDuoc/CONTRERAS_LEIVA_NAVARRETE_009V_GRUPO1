package cl.duoc.airflytrip.auth.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

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
    void registerShouldNormalizeAndPersistClientUser() {
        RegisterRequest request = RegisterRequest.builder()
                .email("  USER@Example.com ")
                .password("secret123")
                .firstName("  Ana ")
                .lastName("  Perez ")
                .documentNumber(" 12.345.678-9 ")
                .phone(" +56 9 1234 5678 ")
                .build();

        when(appUserRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            user.setId(10L);
            user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 12, 0));
            return user;
        });

        UserResponse response = authService.register(request);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());

        AppUser saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("user@example.com");
        assertThat(saved.getPasswordHash()).isEqualTo("encoded-secret");
        assertThat(saved.getFirstName()).isEqualTo("Ana");
        assertThat(saved.getLastName()).isEqualTo("Perez");
        assertThat(saved.getDocumentNumber()).isEqualTo("12.345.678-9");
        assertThat(saved.getPhone()).isEqualTo("+56 9 1234 5678");
        assertThat(saved.getRole()).isEqualTo(UserRole.CLIENT);
        assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(saved.getEnabled()).isTrue();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo("user@example.com");
        assertThat(response.getRole()).isEqualTo("CLIENT");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 12, 0));
    }

    @Test
    void registerShouldRejectDuplicatedEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .email("user@example.com")
                .password("secret123")
                .firstName("Ana")
                .lastName("Perez")
                .build();

        when(appUserRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email is already registered");

        verify(passwordEncoder, never()).encode(anyString());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void loginShouldReturnBearerTokenAndMappedUser() {
        LoginRequest request = LoginRequest.builder()
                .email("  ADMIN@Example.com ")
                .password("secret123")
                .build();

        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        AppUser user = sampleUser(
                20L,
                "admin@example.com",
                "Admin",
                "User",
                UserRole.ADMIN,
                UserStatus.ACTIVE,
                true
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(appUserRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        assertThat(authCaptor.getValue().getName()).isEqualTo("admin@example.com");
        assertThat(authCaptor.getValue().getCredentials()).isEqualTo("secret123");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser().getEmail()).isEqualTo("admin@example.com");
        assertThat(response.getUser().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void loginShouldTranslateBadCredentialsToUnauthorized() {
        LoginRequest request = LoginRequest.builder()
                .email("user@example.com")
                .password("wrong-password")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or password");

        verifyNoInteractions(appUserRepository, jwtService);
    }

    @Test
    void createUserShouldAllowAdminToCreateOperator() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("  operator@Example.com ")
                .password("secret123")
                .firstName("  Camila ")
                .lastName("  Rojas ")
                .documentNumber(" 11.111.111-1 ")
                .phone(" +56 9 2222 2222 ")
                .role("operator")
                .build();

        AppUser creator = sampleUser(
                1L,
                "admin@example.com",
                "Admin",
                "User",
                UserRole.ADMIN,
                UserStatus.ACTIVE,
                true
        );

        when(appUserRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(creator));
        when(appUserRepository.existsByEmail("operator@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser user = invocation.getArgument(0);
            user.setId(30L);
            user.setCreatedAt(LocalDateTime.of(2026, 1, 2, 9, 30));
            return user;
        });

        UserResponse response = authService.createUser(request, "ADMIN@Example.com ");

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());

        AppUser saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("operator@example.com");
        assertThat(saved.getRole()).isEqualTo(UserRole.OPERATOR);
        assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(saved.getEnabled()).isTrue();
        assertThat(saved.getPasswordHash()).isEqualTo("encoded-secret");
        assertThat(response.getEmail()).isEqualTo("operator@example.com");
        assertThat(response.getRole()).isEqualTo("OPERATOR");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 2, 9, 30));
    }

    @Test
    void createUserShouldRejectOperatorCreatingAdmin() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("admin2@example.com")
                .password("secret123")
                .firstName("Admin")
                .lastName("Two")
                .role("ADMIN")
                .build();

        AppUser creator = sampleUser(
                2L,
                "operator@example.com",
                "Operator",
                "User",
                UserRole.OPERATOR,
                UserStatus.ACTIVE,
                true
        );

        when(appUserRepository.findByEmail("operator@example.com")).thenReturn(Optional.of(creator));

        assertThatThrownBy(() -> authService.createUser(request, "operator@example.com"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("You do not have permission to create a user with role ADMIN");

        verify(appUserRepository, never()).existsByEmail(anyString());
        verify(appUserRepository, never()).save(any());
    }

    private AppUser sampleUser(
            Long id,
            String email,
            String firstName,
            String lastName,
            UserRole role,
            UserStatus status,
            Boolean enabled
    ) {
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
