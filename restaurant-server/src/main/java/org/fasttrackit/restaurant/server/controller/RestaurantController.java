package org.fasttrackit.restaurant.server.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.fasttrackit.restaurant.api.model.PageInfo;
import org.fasttrackit.restaurant.api.model.Restaurant;
import org.fasttrackit.restaurant.api.model.RestaurantFilter;
import org.fasttrackit.restaurant.server.model.RestaurantEntity;
import org.fasttrackit.restaurant.server.model.mapper.RestaurantMapper;
import org.fasttrackit.restaurant.server.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("restaurants")
public class RestaurantController {
    private final RestaurantService service;
    private final RestaurantMapper mapper;

    @GetMapping
    public PageInfo<Restaurant> getAll(Pageable pageable, RestaurantFilter filter) {
        return service.getAll(pageable, filter);
    }

    @GetMapping("{id}")
    public Restaurant getOne(@PathVariable Long id) {
        return mapper.toApi(service.getRestaurant(id));
    }

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant newEntity) {
        return mapper.toApi(service.add(mapper.toEntity(newEntity)));
    }

    @PutMapping("{id}")
    public Restaurant replaceEntity(@PathVariable Long id, @RequestBody Restaurant newEntity) {
        return mapper.toApi(service.replace(id, mapper.toEntity(newEntity)));
    }

    @PatchMapping("{id}")
    public Restaurant patchEntity(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        return mapper.toApi(service.updateEntity(id, jsonPatch));
    }

    @DeleteMapping("{id}")
    public Restaurant delete(@PathVariable Long id) {
        return mapper.toApi(service.delete(id));
    }
}
