package com.jjangiji.hankkimoa.restaurant.repository;

import com.jjangiji.hankkimoa.config.RepositoryTest;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.expense.repository.ExpenseRepository;
import com.jjangiji.hankkimoa.expense.repository.ExpenseSavingGoalRepository;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantAverageRatingResponse;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;

class RestaurantRepositoryTest extends RepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;

    @DisplayName("식당별 유저 평점 조회 성공")
    @Test
    void findRestaurantAverageRating() {
        // given
        String expectedUniqueId = "100000";
        User user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        ExpenseSavingGoal expenseSavingGoal = expenseSavingGoalRepository.save(expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user, 70_000, LocalDate.now(), LocalDate.now().plusDays(6))));
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        Restaurant restaurant  = restaurantRepository.save(new Restaurant(category, "한끼식당", expectedUniqueId, 10000, null));
        Expense expense = expenseRepository.save(new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", LocalDate.now(), 5));

        // when
        List<RestaurantAverageRatingResponse> result = restaurantRepository.findRestaurantAverageRating(user.getId());

        // then
        Assertions.assertThat(result.get(0).uniqueId()).isEqualTo(expectedUniqueId);
    }
}
