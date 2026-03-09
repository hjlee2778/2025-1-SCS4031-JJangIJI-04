package com.jjangiji.hankkimoa.user.repository;

import com.jjangiji.hankkimoa.user.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
    Optional<Bookmark> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
    List<Bookmark> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
