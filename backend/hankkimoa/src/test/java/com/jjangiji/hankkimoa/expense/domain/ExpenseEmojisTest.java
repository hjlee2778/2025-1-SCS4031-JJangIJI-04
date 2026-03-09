package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class ExpenseEmojisTest {

    private final User user = new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER);
    private final ExpenseSavingGoal expenseSavingGoal = new ExpenseSavingGoal(1L, user, 80_000, LocalDate.now(), LocalDate.now().plusDays(7));
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final Restaurant restaurant = new Restaurant(1L, new Category(CategoryType.한식), "한끼식당", "12345", 10000, address);
    private final LocalDate now = LocalDate.now();
    private final Expense expense = new Expense(expenseSavingGoal, restaurant, "은화수식당", "돈가스", 10_000, "냠냠굿", now.minusDays(1), 5);

    @DisplayName("지출 내역에 대한 이모지 아이디 조회 성공")
    @Test
    void getEmojiIds() {
        // given
        ExpenseEmoji expenseEmoji1 = new ExpenseEmoji(user, expense, 1);
        ExpenseEmoji expenseEmoji2 = new ExpenseEmoji(user, expense, 2);
        ExpenseEmojis expenseEmojis = new ExpenseEmojis(List.of(expenseEmoji1, expenseEmoji2));

        // when
        List<Integer> result = expenseEmojis.getEmojiIds();

        // then
        Assertions.assertThat(result).containsExactly(1, 2);
    }

    @DisplayName("이모지에 대한 사용자 아이디 조회 성공")
    @Test
    void getUserIds() {
        User user2 = new User("hankkimoa2@gmail.com", "한끼2", "hankkiImage", LoginType.KAKAO, Role.USER);
        ExpenseEmoji expenseEmojiUser1 = new ExpenseEmoji(user, expense, 1);
        ExpenseEmoji expenseEmojiUser2 = new ExpenseEmoji(user2, expense, 1);
        ExpenseEmojis expenseEmojis = new ExpenseEmojis(List.of(expenseEmojiUser1, expenseEmojiUser2));

        // when
        List<Long> result = expenseEmojis.getUserIds(1);

        // then
        Assertions.assertThat(result).containsExactly(user.getId(), user2.getId());
    }
}
