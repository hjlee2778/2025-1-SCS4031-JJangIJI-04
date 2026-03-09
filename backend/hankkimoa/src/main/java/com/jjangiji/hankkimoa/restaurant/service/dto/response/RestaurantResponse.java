package com.jjangiji.hankkimoa.restaurant.service.dto.response;

import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import java.util.List;

public record RestaurantResponse(Long id,
                                 String name,
                                 Integer menuAverage,
                                 List<String> imgUrl,
                                 String streetAddress,
                                 List<String> openingHour,
                                 String category,
                                 List<MenuResponse> menu,
                                 boolean bookmarked) {

    public RestaurantResponse(
            Restaurant restaurant,
            List<String> imageUrls,
            List<String> formattedOpeningHours,
            List<MenuResponse> menuResponses,
            boolean bookmarked)
    {
        this(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getMenuAverage(),
            imageUrls,
            restaurant.getStreetAddress(),
            formattedOpeningHours,
            restaurant.getCategoryName(),
            menuResponses,
            bookmarked);
    }
}
