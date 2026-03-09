package com.jjangiji.hankkimoa.restaurant.service.dto.response;


import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;

public record RestaurantSimpleResponse(Long id,
                                       String name,
                                       Integer menuAverage,
                                       String imgUrl,
                                       String streetAddress,
                                       String openingHours,
                                       String category,
                                       boolean bookmarked) {

    public RestaurantSimpleResponse(
            Restaurant restaurant,
            String restaurantImage,
            String openingHours,
            boolean bookmarked)
    {
        this(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getMenuAverage(),
                restaurantImage,
                restaurant.getStreetAddress(),
                openingHours,
                restaurant.getCategoryName(),
                bookmarked
        );
    }
}
