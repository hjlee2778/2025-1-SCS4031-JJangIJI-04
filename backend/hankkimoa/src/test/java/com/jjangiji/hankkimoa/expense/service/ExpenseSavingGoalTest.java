package com.jjangiji.hankkimoa.expense.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.expense.repository.ExpenseRepository;
import com.jjangiji.hankkimoa.expense.repository.ExpenseSavingGoalRepository;
import com.jjangiji.hankkimoa.expense.service.dto.request.ExpenseSavingGoalCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.RemainingBudgetResponse;
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

class ExpenseSavingGoalTest extends IntegrationTest {

    @Autowired
    private ExpenseSavingGoalService expenseSavingGoalService;
    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private final LocalDate now = LocalDate.now();
    private final LocalDate sevenDayAfter = now.plusDays(6);

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
    }

    @DisplayName("지출 목표 금액 추가 성공")
    @Test
    void createExpenseSavingGoal() {
        // given
        ExpenseSavingGoalCreateRequest request = new ExpenseSavingGoalCreateRequest(100_000, now, sevenDayAfter);

        // when & then
        Assertions.assertThatCode(() -> expenseSavingGoalService.createExpenseSavingGoal(user, request))
                .doesNotThrowAnyException();;
    }

    @DisplayName("지출 목표 금액 추가 실패 : 이미 지출이 존재할 때")
    @Test
    void failWhenExpenseSavingGoalAlreadyExist() {
        // given
        ExpenseSavingGoalCreateRequest request = new ExpenseSavingGoalCreateRequest(100_000, now, sevenDayAfter);
        expenseSavingGoalService.createExpenseSavingGoal(user, request);

        // when & then
        Assertions.assertThatCode(() -> expenseSavingGoalService.createExpenseSavingGoal(user, request))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.EXPENSE_SAVING_GOAL_ALREADY_EXIST.getMessage());
    }

    @DisplayName("가용 금액 조회 성공")
    @Test
    void readRemainingBudget() {
        // given
        int budget = 100_000;
        int expense = 10_000;
        ExpenseSavingGoal expenseSavingGoal = expenseSavingGoalRepository.save(new ExpenseSavingGoal(user, budget, now, sevenDayAfter));
        expenseRepository.save(new Expense(expenseSavingGoal, null, "닭한마리", "닭한마리", expense, "오랜만에 닭한마리", now, 5));

        // when
        RemainingBudgetResponse result = expenseSavingGoalService.readRemainingBudget(user, now);

        // then
        Assertions.assertThat(result.remainingBudget()).isEqualTo(budget - expense);
    }
}
