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
import java.time.LocalDate;

class ExpenseEmojiRepositoryTest extends RepositoryTest {

    private User user1;
    private User user2;
    private ExpenseSavingGoal expenseSavingGoal1;
    private ExpenseSavingGoal expenseSavingGoal2;
    private Restaurant restaurant;
    private Expense expense1;
    private Expense expense2;
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final LocalDate now = LocalDate.now();

    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseEmojiRepository expenseEmojiRepository;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        user2 = userRepository.save(new User("hankkimoa22@gmail.com", "한끼22", "hankkiImage", LoginType.KAKAO, Role.USER));
        expenseSavingGoal1 = expenseSavingGoalRepository.save(new ExpenseSavingGoal(user1, 80_000,
                LocalDate.now(), LocalDate.now().plusDays(6)));
        expenseSavingGoal2 = expenseSavingGoalRepository.save(new ExpenseSavingGoal(user2, 180_000,
                LocalDate.now(), LocalDate.now().plusDays(6)));
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        restaurant  = restaurantRepository.save(new Restaurant(category, "한끼식당", "12345", 10000, address));
        expense1 = expenseRepository.save(new Expense(expenseSavingGoal1, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", now, 5));
        expense2 = expenseRepository.save(new Expense(expenseSavingGoal2, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", now, 5));
    }

    @DisplayName("지출 이모지 존재 여부 반환 성공 : 이미 있는 경우")
    @Test
    void emojiExist() {
        // given
        expenseEmojiRepository.save(new ExpenseEmoji(user1, expense1, 1));

        // when
        boolean result = expenseEmojiRepository.existsExpenseEmojiByExpenseAndUserAndEmojiId(expense1, user1, 1);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("지출 이모지 존재 여부 반환 성공 : 없는 경우")
    @Test
    void emojiNotExist() {
        // given & when
        boolean result = expenseEmojiRepository.existsExpenseEmojiByExpenseAndUserAndEmojiId(expense1, user1, 1);

        // then
        Assertions.assertThat(result).isFalse();
    }
}
