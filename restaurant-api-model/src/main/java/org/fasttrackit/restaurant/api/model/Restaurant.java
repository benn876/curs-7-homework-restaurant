package org.fasttrackit.restaurant.api.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Restaurant(long id, int stars, String name, String city, LocalDate since) {

}
