package org.fasttrackit.restaurant.api.model;

import lombok.Builder;

import java.util.List;

@Builder
public record RestaurantFilter(List<Integer> stars, String city) {
}
