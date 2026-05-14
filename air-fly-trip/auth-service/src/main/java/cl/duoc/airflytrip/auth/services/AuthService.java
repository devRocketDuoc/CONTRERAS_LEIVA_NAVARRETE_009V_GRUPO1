package cl.duoc.airflytrip.auth.services;

import cl.duoc.airflytrip.auth.dtos.request.CreateUserRequest;
import cl.duoc.airflytrip.auth.dtos.request.LoginRequest;
import cl.duoc.airflytrip.auth.dtos.request.RegisterRequest;
import cl.duoc.airflytrip.auth.dtos.response.AuthResponse;
import cl.duoc.airflytrip.auth.dtos.response.UserResponse;
import cl.duoc.airflytrip.auth.exceptions.BadRequestException;
import cl.duoc.airflytrip.auth.exceptions.ConflictException;
import cl.duoc.airflytrip.auth.exceptions.ForbiddenException;
import cl.duoc.airflytrip.auth.exceptions.NotFoundException;
import cl.duoc.airflytrip.auth.exceptions.UnauthorizedException;
import cl.duoc.airflytrip.auth.models.AppUser;
import cl.duoc.airflytrip.auth.models.UserRole;
import cl.duoc.airflytrip.auth.models.UserStatus;
import cl.duoc.airflytrip.auth.repositories.AppUserRepository;
import cl.duoc.airflytrip.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email is already registered");
        }

        AppUser user = AppUser.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .documentNumber(trimToNull(request.getDocumentNumber()))
                .phone(trimToNull(request.getPhone()))
                .role(UserRole.CLIENT)
                .status(UserStatus.ACTIVE)
                .enabled(true)
                .build();

        return toResponse(appUserRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw new UnauthorizedException("Invalid email or password");
        } catch (DisabledException | LockedException exception) {
            throw new UnauthorizedException("User account is disabled or inactive");
        } catch (AuthenticationException exception) {
            throw new UnauthorizedException("Authentication failed");
        }

        AppUser user = appUserRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!Boolean.TRUE.equals(user.getEnabled()) || user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("User account is disabled or inactive");
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(toResponse(user))
                .build();
    }

    public UserResponse getCurrentUser(String email) {
        AppUser user = appUserRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
        return toResponse(user);
    }

    public UserResponse findUserById(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request, String creatorEmail) {
        AppUser creator = appUserRepository.findByEmail(normalizeEmail(creatorEmail))
                .orElseThrow(() -> new NotFoundException("Creator user not found"));

        UserRole targetRole = resolveRole(request.getRole());
        validateUserCreationPermission(creator.getRole(), targetRole);

        String normalizedEmail = normalizeEmail(request.getEmail());
        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email is already registered");
        }

        AppUser user = AppUser.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .documentNumber(trimToNull(request.getDocumentNumber()))
                .phone(trimToNull(request.getPhone()))
                .role(targetRole)
                .status(UserStatus.ACTIVE)
                .enabled(true)
                .build();

        return toResponse(appUserRepository.save(user));
    }

    private UserRole resolveRole(String roleValue) {
        if (roleValue == null || roleValue.isBlank()) {
            return UserRole.CLIENT;
        }

        try {
            return UserRole.valueOf(roleValue.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Invalid role. Allowed values: ADMIN, OPERATOR, CLIENT");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateUserCreationPermission(UserRole creatorRole, UserRole targetRole) {
        if (creatorRole == UserRole.ADMIN) {
            return;
        }

        if (creatorRole == UserRole.OPERATOR && targetRole == UserRole.CLIENT) {
            return;
        }

        throw new ForbiddenException("You do not have permission to create a user with role " + targetRole.name());
    }

    private UserResponse toResponse(AppUser user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .documentNumber(user.getDocumentNumber())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
