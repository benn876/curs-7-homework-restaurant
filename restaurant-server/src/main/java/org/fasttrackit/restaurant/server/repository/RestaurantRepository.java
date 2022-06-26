package org.fasttrackit.restaurant.server.repository;

import org.fasttrackit.restaurant.server.model.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}
