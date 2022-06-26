package org.fasttrackit.restaurant.api.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PageInfo<T>(int totalPages, int pageSize, int pageNumber, int numberOfElements, List<T> contents) {
}
