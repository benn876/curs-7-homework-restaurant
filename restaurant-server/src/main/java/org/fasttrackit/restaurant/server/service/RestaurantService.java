package org.fasttrackit.restaurant.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.fasttrackit.restaurant.api.model.PageInfo;
import org.fasttrackit.restaurant.api.model.Restaurant;
import org.fasttrackit.restaurant.api.model.RestaurantFilter;
import org.fasttrackit.restaurant.client.exceptions.ResourceNotFound;
import org.fasttrackit.restaurant.client.exceptions.ValidationException;
import org.fasttrackit.restaurant.server.model.RestaurantEntity;
import org.fasttrackit.restaurant.server.model.mapper.RestaurantMapper;
import org.fasttrackit.restaurant.server.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.time.LocalDate.now;

@Component
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public PageInfo<Restaurant> getAll(Pageable pageable, RestaurantFilter filter) {
        Page<RestaurantEntity> page = repository.findAll(pageable);

        return new PageInfo<>(page.getTotalPages(), pageable.getPageSize(), page.getNumber(), page.getNumberOfElements(),
                mapper.toApi(page.getContent().stream()
                        .filter(restaurant -> filter == null || filter.stars() == null || filter.stars().contains(restaurant.getStars()))
                        .filter(restaurant -> filter == null || filter.city() == null || filter.city().equals(restaurant.getCity()))
                        .toList())
        );

    }

    public RestaurantEntity getRestaurant(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Could not find restaurant with id %s".formatted(id)));
    }

    public RestaurantEntity add(RestaurantEntity restaurantEntity) {
        validateRequestData(restaurantEntity);
        return repository.save(restaurantEntity.withId(0));
    }

    private void validateRequestData(RestaurantEntity restaurantEntity) {
        if (restaurantEntity.getSince().isAfter(now())) {
            throw new ValidationException("You cannot add a restaurant in the future");
        }

        Optional<Restaurant> existingRestaurant = getAll(Pageable.unpaged(), null).contents().stream()
                .filter(restaurant -> restaurant.name().equals(restaurantEntity.getName()))
                .findFirst();

        if (existingRestaurant.isPresent()) {
            throw new ValidationException("There is already a restaurant with name: %s".formatted(restaurantEntity.getName()));
        }

    }

    public RestaurantEntity replace(Long id, RestaurantEntity newEntity) {
        RestaurantEntity restaurantEntity = getRestaurant(id);
        return repository.save(restaurantEntity
                .withCity(newEntity.getCity())
                .withName(newEntity.getName())
                .withStars(newEntity.getStars())
        );
    }

    public RestaurantEntity updateEntity(Long id, JsonPatch jsonPatch) {
        return repository.findById(id)
                .map(restaurantEntity -> applyPatch(restaurantEntity, jsonPatch))
                .map(restaurantEntity -> replace(id, restaurantEntity))
                .orElseThrow(() -> new ResourceNotFound("Could not find restaurant with id %s".formatted(id)));
    }

    private RestaurantEntity applyPatch(RestaurantEntity dbEntity, JsonPatch jsonPatch) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode jsonNode = jsonMapper.convertValue(dbEntity, JsonNode.class);
            JsonNode patchedJson = jsonPatch.apply(jsonNode);
            return jsonMapper.treeToValue(patchedJson, RestaurantEntity.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public RestaurantEntity delete(Long id) {
        RestaurantEntity toBeDeleted = getRestaurant(id);
        repository.delete(toBeDeleted);
        return toBeDeleted;
    }
}
