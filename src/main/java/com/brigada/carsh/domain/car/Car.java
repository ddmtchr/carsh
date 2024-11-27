package com.brigada.carsh.domain.car;

import com.brigada.carsh.domain.location.Location;
import com.brigada.carsh.dto.response.CarResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "car")
@NamedNativeQuery(
        name = "available_cars_by_radius",
        query = "SELECT * FROM find_available_cars(:latitude, :longitude, :radius)",
        resultSetMapping = "car_dto_mapping"
)
@SqlResultSetMapping(
        name = "car_dto_mapping",
        classes = @ConstructorResult(
                targetClass = CarResponseDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "registration_number", type = String.class),
                        @ColumnResult(name = "model", type = String.class),
                        @ColumnResult(name = "car_class", type = CarClass.class),
                        @ColumnResult(name = "fuel_level", type = BigDecimal.class),
                        @ColumnResult(name = "minute_price", type = BigDecimal.class),
                        @ColumnResult(name = "latitude", type = BigDecimal.class),
                        @ColumnResult(name = "longitude", type = BigDecimal.class),
                        @ColumnResult(name = "status", type = CarStatus.class)
                }
        )
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car { // todo cascade везде
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "model", nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_class", nullable = false)
    private CarClass carClass;

    @Column(name = "fuel_level", nullable = false)
    private BigDecimal fuelLevel;

    @Column(name = "minute_price", nullable = false)
    private BigDecimal minutePrice;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status;
}
