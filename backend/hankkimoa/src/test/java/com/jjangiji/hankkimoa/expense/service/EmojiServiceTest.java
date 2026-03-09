package com.jjangiji.hankkimoa.expense.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.expense.repository.ExpenseRepository;
import com.jjangiji.hankkimoa.expense.repository.ExpenseSavingGoalRepository;
import com.jjangiji.hankkimoa.expense.service.dto.request.EmojiCreateRequest;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
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

class EmojiServiceTest extends IntegrationTest {

    @Autowired
    private EmojiService emojiService;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Restaurant restaurant;
    private ExpenseSavingGoal expenseSavingGoal;
    private Expense expense;
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final LocalDate now = LocalDate.now();
    private final LocalDate sevenDayAfter = now.plusDays(6);

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        restaurant  = restaurantRepository.save(new Restaurant(category, "한끼식당", "12345", 10000, address));
        expenseSavingGoal = expenseSavingGoalRepository.save(expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user, 70_000, now, sevenDayAfter)));
        expense = expenseRepository.save(new Expense(expenseSavingGoal, restaurant,
                "한끼식당", "순두부", 8_000,
                "든든하게 먹음!", now, 5));
    }

    @DisplayName("이모지 추가 성공")
    @Test
    void createEmoji() {
        // given & when & then
        EmojiCreateRequest request = new EmojiCreateRequest(expense.getId(), 1);
        Assertions.assertThatCode(() -> emojiService.createEmoji(user, request))
                .doesNotThrowAnyException();
    }

    @DisplayName("이모지 추가 실패 : 이미 이모지가 존재하는 경우")
    @Test
    void failWhenEmojiAlreadyExist() {
        // given
        EmojiCreateRequest request = new EmojiCreateRequest(expense.getId(), 1);
        emojiService.createEmoji(user, request);

        // when & then
        Assertions.assertThatCode(() -> emojiService.createEmoji(user, request))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.EMOJI_ALREADY_EXIST.getMessage());
    }
}
