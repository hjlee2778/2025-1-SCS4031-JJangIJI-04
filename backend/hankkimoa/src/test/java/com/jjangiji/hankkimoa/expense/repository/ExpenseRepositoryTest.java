package com.jjangiji.hankkimoa.expense.repository;

import com.jjangiji.hankkimoa.config.RepositoryTest;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseEmoji;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;

class ExpenseRepositoryTest extends RepositoryTest {

    private User user;
    private ExpenseSavingGoal expenseSavingGoal;
    private Restaurant restaurant;
    private final LocalDate now = LocalDate.now();
    private final LocalDate before = now.minusDays(1);

    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseEmojiRepository expenseEmojiRepository;
    @Autowired
    private UserRepository userRepository;

    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        expenseSavingGoal = expenseSavingGoalRepository.save(new ExpenseSavingGoal(user, 80_000, LocalDate.now(), LocalDate.now().plusDays(7)));
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        restaurant  = restaurantRepository.save(new Restaurant(category, "한끼식당", "12345", 10000, address));
    }

    @DisplayName("지출 한달 내역 조회 성공")
    @Test
    void findAllWithSavingGoalAndUserByExpenseDateOrderByExpenseDateAsc() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", before, 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", now, 5);
        expenseRepository.saveAll(List.of(expense1, expense2));

        // when
        List<Expense> results = expenseRepository.findAllByExpenseDateOrderByExpenseDateAsc(user.getId(), before, now);

        // then
        Assertions.assertThat(results).containsExactly(expense1, expense2);
    }

    @DisplayName("지출 한달 내역 조회 성공 : 주어진 날짜 범위 벗어난 경우")
    @Test
    void findAllWithSavingGoalAndUserByExpenseDateOrderByExpenseDateAsc_withOutOfRange() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", before.minusDays(1), 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", now, 5);
        expenseRepository.saveAll(List.of(expense1, expense2));

        // when
        List<Expense> results = expenseRepository.findAllByExpenseDateOrderByExpenseDateAsc(user.getId(), before, now);

        // then
        Assertions.assertThat(results).containsExactly(expense2);
    }

    @DisplayName("이모지 누른 지출 내역들 조회 성공")
    @Test
    void findAllByReactedEmoji() {
        // given
        Expense expense = new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", before.minusDays(1), 5);
        expenseRepository.save(expense);

        ExpenseEmoji expenseEmoji1 = new ExpenseEmoji(user, expense, 1);
        ExpenseEmoji expenseEmoji2 = new ExpenseEmoji(user, expense, 2);
        expenseEmojiRepository.saveAll(List.of(expenseEmoji1, expenseEmoji2));

        // when
        List<Expense> result = expenseRepository.findAllByReactedEmoji(user.getId());

        // then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result).containsExactly(expense);
    }

    @DisplayName("커뮤니티 지출 내역 조회")
    @Test
    void findAllWithSavingGoalAndUser() {
        // given
        Expense expense1 = expenseRepository.save(
                new Expense(expenseSavingGoal, restaurant, "산타돈부리", "사케동", 13_000, "사케동 맛있다 ~", now, 5));
        Expense expense2 = expenseRepository.save(
                new Expense(expenseSavingGoal, restaurant, "하얀집", "복소사", 10_000, "가성비 짱!", now, 5));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        // when
        Slice<Expense> results = expenseRepository.findAllWithSavingGoalAndUser(pageable);

        // then
        Assertions.assertThat(results).containsExactly(expense2, expense1);
    }
}
