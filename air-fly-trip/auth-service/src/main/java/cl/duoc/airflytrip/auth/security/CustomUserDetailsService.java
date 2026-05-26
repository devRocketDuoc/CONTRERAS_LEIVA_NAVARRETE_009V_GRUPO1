package cl.duoc.airflytrip.auth.security;

import cl.duoc.airflytrip.auth.models.AppUser;
import cl.duoc.airflytrip.auth.models.UserStatus;
import cl.duoc.airflytrip.auth.repositories.AppUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        boolean active = Boolean.TRUE.equals(user.getEnabled()) && user.getStatus() == UserStatus.ACTIVE;
        boolean accountLocked = user.getStatus() == UserStatus.BLOCKED;

        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .disabled(!active)
                .accountLocked(accountLocked)
                .build();
    }
}
