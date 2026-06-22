package cl.duoc.airflytrip.reservations;

import cl.duoc.airflytrip.reservations.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Disabled("Legacy context-load placeholder replaced by focused unit tests")
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
class ReservationServiceApplicationTests {

    @MockitoBean
    private ReservationRepository reservationRepository;

    @Test
    void contextLoads() {
    }

}
