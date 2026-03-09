package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class ExpenseSavingGoalTest {

    private final User user = new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER);
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final ExpenseSavingGoal expenseSavingGoal = new ExpenseSavingGoal(1L, user, 80_000, LocalDate.now(), LocalDate.now().plusDays(7));
    private final Restaurant restaurant = new Restaurant(1L, new Category(CategoryType.한식), "한끼식당", "12345", 10000, address);
    private final LocalDate now = LocalDate.now();

    @DisplayName("남은 예산 계산 성공")
    @Test
    void calculateRemainingBudget() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant, "은화수식당", "돈가스", 10_000, "냠냠굿", now, 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant, "왕순이김밥", "김밥", 5_000, "가성비굿", now, 5);

        // when
        int remainingBudget = expenseSavingGoal.calculateRemainingBudget(List.of(expense1, expense2));

        // then
        Assertions.assertThat(remainingBudget).isEqualTo(65_000);
    }

    @DisplayName("절약 목표 금액 생성 실패 : 시작일이 종료일보다 앞서는 경우")
    @Test
    void failWhenStartDatePassEndDate() {
        // given & when & then
        Assertions.assertThatCode(() -> new ExpenseSavingGoal(user, 100_000, now.plusDays(1), now))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.DATE_RANGE_INVALID.getMessage());
    }

    @DisplayName("지출 목표 금액 날짜비교 여부 반환 : 이전 지출 목표 금액일 종료날짜와 생성하려는 지출 목표 금액 시작날짜가 같은 경우")
    @Test
    void isAfter_false() {
        // given
        ExpenseSavingGoal expenseSavingGoal1 = new ExpenseSavingGoal(
                user, 100_000,
                LocalDate.of(2025, 4, 20), LocalDate.of(2025, 4, 26));
        ExpenseSavingGoal expenseSavingGoal2 = new ExpenseSavingGoal(
                user, 100_000,
                LocalDate.of(2025, 4, 26), LocalDate.of(2025, 5, 3));

        // when
        boolean result = expenseSavingGoal2.isAfter(expenseSavingGoal1);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @DisplayName("지출 목표 금액 날짜비교 여부 반환 : 이전 지출 목표 금액일 종료날짜가 생성하려는 지출 목표 금액 시작날짜 이전인 경우")
    @Test
    void isAfter_true() {
        // given
        ExpenseSavingGoal expenseSavingGoal1 = new ExpenseSavingGoal(
                user, 100_000,
                LocalDate.of(2025, 4, 20), LocalDate.of(2025, 4, 26));
        ExpenseSavingGoal expenseSavingGoal2 = new ExpenseSavingGoal(
                user, 100_000,
                LocalDate.of(2025, 4, 27), LocalDate.of(2025, 5, 3));

        // when
        boolean result = expenseSavingGoal2.isAfter(expenseSavingGoal1);

        // then
        Assertions.assertThat(result).isTrue();
    }
}
