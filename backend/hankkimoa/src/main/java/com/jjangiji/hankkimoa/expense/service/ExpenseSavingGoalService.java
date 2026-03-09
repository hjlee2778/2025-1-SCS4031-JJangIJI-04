package com.jjangiji.hankkimoa.expense.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.expense.repository.ExpenseRepository;
import com.jjangiji.hankkimoa.expense.repository.ExpenseSavingGoalRepository;
import com.jjangiji.hankkimoa.expense.service.dto.request.ExpenseSavingGoalCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.ExpenseSavingGoalCreateResponse;
import com.jjangiji.hankkimoa.expense.service.dto.response.RemainingBudgetResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseSavingGoalService {

    private final ExpenseSavingGoalRepository expenseSavingGoalRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public ExpenseSavingGoalCreateResponse createExpenseSavingGoal(User user, ExpenseSavingGoalCreateRequest request) {
        ExpenseSavingGoal expenseSavingGoal = new ExpenseSavingGoal(user, request.budget(), request.startDate(), request.endDate());
        validateExpenseSavingGoalExist(user, expenseSavingGoal);

        ExpenseSavingGoal savedExpenseSavingGoal = expenseSavingGoalRepository.save(expenseSavingGoal);
        return new ExpenseSavingGoalCreateResponse(savedExpenseSavingGoal.getId());
    }

    private void validateExpenseSavingGoalExist(User user, ExpenseSavingGoal expenseSavingGoal) {
        Optional<ExpenseSavingGoal> optionalExpenseSavingGoal = expenseSavingGoalRepository.findLastByUser(user.getId());
        if (optionalExpenseSavingGoal.isEmpty()) return;

        ExpenseSavingGoal lastExpenseSavingGoal = optionalExpenseSavingGoal.get();
        if (!expenseSavingGoal.isAfter(lastExpenseSavingGoal)) {
            throw new HankkiMoaException(ExceptionCode.EXPENSE_SAVING_GOAL_ALREADY_EXIST);
        }
    }

    @Transactional(readOnly = true)
    public RemainingBudgetResponse readRemainingBudget(User user, LocalDate date) {
        ExpenseSavingGoal expenseSavingGoal = readExpenseSavingGoal(user.getId(), date);
        List<Expense> savingGoalExpenses = expenseRepository.findAllByExpenseSavingGoal(expenseSavingGoal);

        int remainingBudget = expenseSavingGoal.calculateRemainingBudget(savingGoalExpenses);
        return new RemainingBudgetResponse(remainingBudget);
    }

    private ExpenseSavingGoal readExpenseSavingGoal(Long userId, LocalDate date) {
        return expenseSavingGoalRepository.findByUserAndDate(userId, date)
                .orElseThrow(() -> new HankkiMoaException(
                        ExceptionCode.EXPENSE_SAVING_GOAL_NOT_FOUND));
    }
}
