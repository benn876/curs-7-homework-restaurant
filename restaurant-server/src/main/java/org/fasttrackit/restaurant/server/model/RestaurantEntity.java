package org.fasttrackit.restaurant.server.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class RestaurantEntity {
    @Id
    @GeneratedValue
    private long id;
    private int stars;
    private String name;
    private String city;
    private LocalDate since;

    public RestaurantEntity(int stars, String name, String city, LocalDate since) {
        this(0, stars, name, city, since);
    }
}
