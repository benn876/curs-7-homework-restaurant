package org.fasttrackit.restaurant.client;

import lombok.RequiredArgsConstructor;
import org.fasttrackit.restaurant.api.model.PageInfo;
import org.fasttrackit.restaurant.api.model.Restaurant;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class RestaurantClientIT {
    private final RestaurantClientRTConfig config;

    public RestaurantClient restaurants() {
        return new RestaurantClient(config.location());
    }

    public class RestaurantClient {
        private final String url;

        public RestaurantClient(String location) {
            this.url = UriComponentsBuilder.fromHttpUrl(location)
                    .pathSegment("restaurants")
                    .toUriString();
        }

        public Optional<Restaurant> get(Long restaurantId) {
            var url = UriComponentsBuilder.fromHttpUrl(this.url)
                    .pathSegment(valueOf(restaurantId))
                    .toUriString();
            return ofNullable(new RestTemplate().getForObject(url, Restaurant.class));
        }

        public PageInfo<Restaurant> all() {
            return new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(null), new ParameterizedTypeReference<PageInfo<Restaurant>>() {
            }).getBody();
        }
    }
}
