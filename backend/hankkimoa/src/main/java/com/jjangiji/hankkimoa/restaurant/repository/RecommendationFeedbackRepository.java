package com.jjangiji.hankkimoa.restaurant.repository;

import com.jjangiji.hankkimoa.restaurant.domain.RecommendationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecommendationFeedbackRepository extends JpaRepository<RecommendationFeedback, Long> {

    Optional<RecommendationFeedback> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
