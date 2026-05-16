package cl.duoc.airflytrip.chargingstations.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "charging_station")
public class ChargingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "terminal_id", nullable = false)
    private Long terminalId;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private Boolean active;
}
