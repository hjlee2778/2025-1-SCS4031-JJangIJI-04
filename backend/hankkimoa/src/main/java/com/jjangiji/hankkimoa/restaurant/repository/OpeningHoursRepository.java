package com.jjangiji.hankkimoa.restaurant.repository;

import com.jjangiji.hankkimoa.restaurant.domain.OpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpeningHoursRepository extends JpaRepository<OpeningHour, Long> {
    List<OpeningHour> findAllByRestaurantId(Long restaurantId);
}
