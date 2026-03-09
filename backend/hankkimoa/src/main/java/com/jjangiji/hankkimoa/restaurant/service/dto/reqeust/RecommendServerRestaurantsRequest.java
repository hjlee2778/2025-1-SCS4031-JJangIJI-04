package com.jjangiji.hankkimoa.restaurant.service.dto.reqeust;

import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantAverageRatingResponse;
import java.util.List;

public record RecommendServerRestaurantsRequest(Long userId,
                                                int remainingBudget,
                                                List<String> userCategory,
                                                Integer feedback,
                                                List<RestaurantAverageRatingResponse> restaurantRatings) {
}
