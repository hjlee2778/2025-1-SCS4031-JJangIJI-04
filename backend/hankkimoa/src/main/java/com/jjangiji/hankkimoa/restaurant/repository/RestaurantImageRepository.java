package com.jjangiji.hankkimoa.restaurant.repository;

import com.jjangiji.hankkimoa.restaurant.domain.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {

    List<RestaurantImage> findAllByRestaurantId(Long restaurantId);
}
