package com.jjangiji.hankkimoa.restaurant.service;

import com.jjangiji.hankkimoa.expense.service.ExpenseSavingGoalService;
import com.jjangiji.hankkimoa.expense.service.dto.response.RemainingBudgetResponse;
import com.jjangiji.hankkimoa.restaurant.domain.RecommendationFeedback;
import com.jjangiji.hankkimoa.restaurant.repository.RecommendationFeedbackRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RecommendServerRestaurantsRequest;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RecommendationFeedbackRequest;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RecommendServerRestaurantsResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantAverageRatingResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.domain.UserCategory;
import com.jjangiji.hankkimoa.user.repository.UserCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private final RecommendClient recommendClient;
    private final UserCategoryRepository userCategoryRepository;
    private final RecommendationFeedbackRepository recommendationFeedbackRepository;
    private final RestaurantRepository restaurantRepository;
    private final ExpenseSavingGoalService expenseSavingGoalService;

    @Transactional
    public void createRecommendationFeedback(User user, RecommendationFeedbackRequest request) {
        RecommendationFeedback feedback = new RecommendationFeedback(user, request.feedback());
        recommendationFeedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public RecommendServerRestaurantsResponse readRecommendRestaurants(User user) {
        RecommendServerRestaurantsRequest request = toRecommendServerRestaurantRequest(user);
        return recommendClient.requestRecommendRestaurants(request);
    }

    private RecommendServerRestaurantsRequest toRecommendServerRestaurantRequest(User user) {
        List<String> userCategory = userCategoryRepository.findAllByUser(user)
                .stream()
                .map(UserCategory::getCategoryName)
                .toList();
        Integer feedback = recommendationFeedbackRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(RecommendationFeedback::getFeedback)
                .orElse(null);
        List<RestaurantAverageRatingResponse> restaurantAverageRatings = restaurantRepository.findRestaurantAverageRating(user.getId());
        RemainingBudgetResponse remainingBudgetResponse = expenseSavingGoalService.readRemainingBudget(user, LocalDate.now());

        return new RecommendServerRestaurantsRequest(
                user.getId(),
                remainingBudgetResponse.remainingBudget(),
                userCategory,
                feedback,
                restaurantAverageRatings);
    }
}
