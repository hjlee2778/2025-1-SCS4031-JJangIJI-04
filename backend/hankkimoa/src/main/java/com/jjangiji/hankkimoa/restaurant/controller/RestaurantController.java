package com.jjangiji.hankkimoa.restaurant.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.restaurant.service.RecommendService;
import com.jjangiji.hankkimoa.restaurant.service.RestaurantBatchService;
import com.jjangiji.hankkimoa.restaurant.service.RestaurantService;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RecommendationFeedbackRequest;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RestaurantCreateRequest;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RecommendServerRestaurantsResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantSearchResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantSimpleResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RestaurantController {

    private final RecommendService recommendService;
    private final RestaurantBatchService restaurantBatchService;
    private final RestaurantService restaurantService;

    @PostMapping("/api/restaurants")
    public ResponseEntity<Void> createRestaurants(@RequestBody List<RestaurantCreateRequest> request) {
        restaurantBatchService.createRestaurants(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/recommendation/feedback")
    public ResponseEntity<Void> createRecommendationFeedback(@AuthRequiredPrincipal User user,
                                                             @RequestBody RecommendationFeedbackRequest request) {
        recommendService.createRecommendationFeedback(user, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/restaurants/search")
    public ResponseEntity<List<RestaurantSearchResponse>> readRestaurants(@RequestParam("keyword") String keyword) {
        List<RestaurantSearchResponse> restaurants = restaurantService.searchRestaurants(keyword);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/api/recommendation/restaurants")
    public ResponseEntity<List<RestaurantSimpleResponse>> readRecommendRestaurants(@AuthRequiredPrincipal User user) {
        RecommendServerRestaurantsResponse response = recommendService.readRecommendRestaurants(user);
        List<RestaurantSimpleResponse> recommendRestaurants = restaurantService.readRestaurants(user, response.uniqueIds());
        return ResponseEntity.ok(recommendRestaurants);
    }

    @GetMapping("/api/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantResponse> readRestaurant(@AuthRequiredPrincipal User user,
                                                             @PathVariable Long restaurantId) {
        RestaurantResponse restaurantResponse = restaurantService.readRestaurant(user, restaurantId);
        return ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping("/api/bookmarks/restaurants")
    public ResponseEntity<List<RestaurantSimpleResponse>> readRestaurant(@AuthRequiredPrincipal User user) {
        List<RestaurantSimpleResponse> bookmarkedRestaurants = restaurantService.readBookmarkedRestaurants(user);
        return ResponseEntity.ok(bookmarkedRestaurants);
    }
}
