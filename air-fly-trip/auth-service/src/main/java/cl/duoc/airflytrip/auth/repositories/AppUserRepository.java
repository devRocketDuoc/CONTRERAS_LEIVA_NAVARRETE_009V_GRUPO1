package cl.duoc.airflytrip.auth.repositories;

import cl.duoc.airflytrip.auth.models.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
