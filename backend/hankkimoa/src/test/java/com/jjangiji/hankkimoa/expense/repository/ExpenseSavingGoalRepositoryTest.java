package com.jjangiji.hankkimoa.expense.repository;

import com.jjangiji.hankkimoa.config.RepositoryTest;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
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

class ExpenseSavingGoalRepositoryTest extends RepositoryTest {

    @Autowired
    private ExpenseSavingGoalRepository expenseSavingGoalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    private User user;
    private User user2;
    private final LocalDate now = LocalDate.now();

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        user2 = userRepository.save(new User("hankkimoa2@gmail.com", "한끼2", "hankkiImage", LoginType.KAKAO, Role.USER));
    }

    @DisplayName("최신 목표 금액 조회")
    @Test
    void findLastByUser() {
        // given
        ExpenseSavingGoal expenseSavingGoal1 = expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user,100_000,
                        LocalDate.of(2025, 4, 20),
                        LocalDate.of(2025, 4, 26)));
        ExpenseSavingGoal expenseSavingGoal2 = expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user,100_000,
                        LocalDate.of(2025, 4, 27),
                        LocalDate.of(2025, 5, 3)));

        expenseSavingGoalRepository.save(expenseSavingGoal1);
        expenseSavingGoalRepository.save(expenseSavingGoal2);

        // when
        ExpenseSavingGoal result = expenseSavingGoalRepository
                .findLastByUser(user.getId())
                .get();

        // then
        Assertions.assertThat(result).isEqualTo(expenseSavingGoal2);
    }

    @DisplayName("절약 목표 금액 조회 : 날짜가 주어진 경우")
    @Test
    void findByUserAndDate() {
        // given
        ExpenseSavingGoal expenseSavingGoal = expenseSavingGoalRepository.save(
                new ExpenseSavingGoal(user,100_000, now, now.plusDays(7)));

        // when
        ExpenseSavingGoal result = expenseSavingGoalRepository
                .findByUserAndDate(user.getId(), now.plusDays(1))
                .get();

        // then
        Assertions.assertThat(result).isEqualTo(expenseSavingGoal);
    }
}
