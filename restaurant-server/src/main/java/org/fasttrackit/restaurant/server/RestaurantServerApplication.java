package org.fasttrackit.restaurant.server;

import org.fasttrackit.restaurant.server.model.RestaurantEntity;
import org.fasttrackit.restaurant.server.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class RestaurantServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServerApplication.class, args);
    }

    @Bean
    CommandLineRunner atStartup(RestaurantRepository repo) {
        return args -> repo.saveAll(List.of(
                new RestaurantEntity(5, "Termal", "Oradea", LocalDate.of(1994, 4, 11)),
                new RestaurantEntity(4, "Continental", "Timisoara", LocalDate.of(1992, 11, 11)),
                new RestaurantEntity(3, "OldMill", "Oradea", LocalDate.of(1997, 4, 22))
        ));
    }

}
