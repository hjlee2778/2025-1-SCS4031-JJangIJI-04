package com.jjangiji.hankkimoa.expense.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.expense.service.ExpenseSavingGoalService;
import com.jjangiji.hankkimoa.expense.service.dto.request.ExpenseSavingGoalCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.ExpenseSavingGoalCreateResponse;
import com.jjangiji.hankkimoa.expense.service.dto.response.RemainingBudgetResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class ExpenseSavingGoalController {

    private final ExpenseSavingGoalService expenseSavingGoalService;

    @PostMapping("/api/saving-goals")
    public ResponseEntity<ExpenseSavingGoalCreateResponse> createExpense(
            @AuthRequiredPrincipal User user,
            @RequestBody ExpenseSavingGoalCreateRequest request) {
        ExpenseSavingGoalCreateResponse response = expenseSavingGoalService.createExpenseSavingGoal(user, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/saving-goals/remaining")
    public ResponseEntity<RemainingBudgetResponse> readRemainingBudget(
            @AuthRequiredPrincipal User user,
            @RequestParam("date")LocalDate date) {
        RemainingBudgetResponse remainingBudget = expenseSavingGoalService.readRemainingBudget(user, date);
        return ResponseEntity.ok(remainingBudget);
    }
}
