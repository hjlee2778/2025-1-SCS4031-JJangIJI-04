package com.jjangiji.hankkimoa.expense.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.expense.repository.ExpenseRepository;
import com.jjangiji.hankkimoa.expense.repository.ExpenseSavingGoalRepository;
import com.jjangiji.hankkimoa.expense.service.dto.request.ExpenseCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.CommunityExpenseResponse;
import com.jjangiji.hankkimoa.expense.service.dto.response.TodayExpenses;
import com.jjangiji.hankkimoa.expense.service.dto.response.MonthlyExpenseResponse;
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
import java.util.List;
import java.util.Optional;

class ExpenseServiceTest extends IntegrationTest {

    @Autowired
    private ExpenseService expenseService;
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
    private final LocalDate now = LocalDate.now();
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final LocalDate sevenDayAfter = now.plusDays(6);

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        restaurant  = restaurantRepository.save(new Restaurant(category, "한끼식당", "12345", 10000, address));
        expenseSavingGoal = expenseSavingGoalRepository.save(new ExpenseSavingGoal(user, 70_000, now, sevenDayAfter));
    }

    @DisplayName("지출 내역 생성 성공")
    @Test
    void createExpense() {
        // given
        ExpenseCreateRequest request = new ExpenseCreateRequest(restaurant.getId(),
                "한끼식당", "순두부찌개", 7000, "든든하게 먹음!", LocalDate.now(), 5);

        // when
        Long expenseId = expenseService.createExpense(user, request);

        // then
        Assertions.assertThat(expenseId).isNotNull();
    }

    @DisplayName("지출 내역 생성 성공 : 식당 정보가 없는 경우")
    @Test
    void createExpenseWhenRestaurantNull() {
        ExpenseCreateRequest request = new ExpenseCreateRequest(null,
                "한끼식당", "순두부찌개", 8000, "든든하게 먹음!", LocalDate.now(), 5);

        // when
        Long expenseId = expenseService.createExpense(user, request);

        // then
        Assertions.assertThat(expenseId).isNotNull();
    }

    @DisplayName("데일리 지출 내역 조회 성공")
    @Test
    void readTodayExpenses() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant, "학식", "라면", 5_000, "오늘은 대충 떼워야지", now.minusDays(1), 4);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant, "닭한마리", "닭한마리", 10_000, "오랜만에 닭한마리", now, 5);
        expenseRepository.saveAll(List.of(expense1, expense2));

        // when
        TodayExpenses todayExpenses = expenseService.readTodayExpenses(user.getId(), now);

        // then
        Assertions.assertThat(todayExpenses.savingGoalStatus().budget()).isEqualTo(70_000);
        Assertions.assertThat(todayExpenses.expenses()).hasSize(1);
    }

    @DisplayName("데일리 지출 내역 조회 성공 : 지출이 존재하지 않는 경우")
    @Test
    void readTodayExpenses_withNoExpenses() {
        // given & when
        TodayExpenses todayExpenses = expenseService.readTodayExpenses(user.getId(), now);

        // then
        Assertions.assertThat(todayExpenses.savingGoalStatus().budget()).isEqualTo(70_000);
        Assertions.assertThat(todayExpenses.expenses()).isEmpty();
    }

    @DisplayName("지출 한달 내역 조회 성공")
    @Test
    void readMonthlyExpenses() {
        // given
        LocalDate sevenBefore = now.minusDays(7);
        ExpenseSavingGoal expenseSavingGoal1 = expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user, 140_000, sevenBefore, now));
        ExpenseSavingGoal expenseSavingGoal2 = expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user, 70_000, now, sevenDayAfter));
        Expense expense1 = new Expense(expenseSavingGoal1, restaurant, "산타돈부리", "사케동", 13_000, "사케동 맛있다 ~", sevenBefore, 5);
        Expense expense2 = new Expense(expenseSavingGoal2, restaurant, "닭한마리", "닭한마리", 12_000, "오랜만에 닭한마리", now, 5);
        expenseSavingGoalRepository.saveAll(List.of(expenseSavingGoal1, expenseSavingGoal2));
        expenseRepository.saveAll(List.of(expense1, expense2));

        // when
        MonthlyExpenseResponse monthlyExpenseResponse = expenseService.readMonthlyExpenses(user.getId(), sevenBefore, now);

        // then
        Assertions.assertThat(monthlyExpenseResponse.dailyExpenseStatus()).hasSize(2);
        Assertions.assertThat(monthlyExpenseResponse.dailyExpenseOverBudgetCount()).isEqualTo(1);
        Assertions.assertThat(monthlyExpenseResponse.monthlyExpenseRecordCount()).isEqualTo(2);
    }

    @DisplayName("커뮤니티 지출 내역 조회")
    @Test
    void readCommunityExpenses() {
        // given
        Expense expense1 = expenseRepository.save(
                new Expense(expenseSavingGoal, restaurant, "산타돈부리", "사케동", 13_000, "사케동 맛있다 ~", now, 5));
        Expense expense2 = expenseRepository.save(
                new Expense(expenseSavingGoal, restaurant, "하얀집", "복소사", 10_000, "가성비 짱!", now, 5));

        // when
        List<CommunityExpenseResponse> communityExpenseResponses = expenseService.readCommunityExpenses(10, 0);

        // then
        Assertions.assertThat(communityExpenseResponses).hasSize(2);
    }

    @DisplayName("지출 내역 삭제 성공")
    @Test
    void deleteExpense() {
        // given
        ExpenseCreateRequest request = new ExpenseCreateRequest(null,
                "한끼식당", "순두부찌개", 8000, "든든하게 먹음!", LocalDate.now(), 5);
        Long expenseId = expenseService.createExpense(user, request);

        // when
        expenseService.deleteExpense(expenseId);
        Optional<Expense> expense = expenseRepository.findById(expenseId);

        // when & then
        Assertions.assertThat(expense).isEmpty();
    }

    @DisplayName("지출 내역 삭제 성공")
    @Test
    void failWhenExpenseNotExist() {
        Assertions.assertThatCode(() -> expenseService.deleteExpense(1L))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.EXPENSE_NOT_FOUND.getMessage());
    }
}
