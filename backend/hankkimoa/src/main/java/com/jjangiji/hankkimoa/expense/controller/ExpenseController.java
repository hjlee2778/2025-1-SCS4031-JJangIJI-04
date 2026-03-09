package com.jjangiji.hankkimoa.expense.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.expense.service.ExpenseService;
import com.jjangiji.hankkimoa.expense.service.dto.request.ExpenseCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.CommunityExpenseResponse;
import com.jjangiji.hankkimoa.expense.service.dto.response.MonthlyExpenseResponse;
import com.jjangiji.hankkimoa.expense.service.dto.response.TodayExpenses;
import com.jjangiji.hankkimoa.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/api/expenses")
    public ResponseEntity<Void> createExpense(@AuthRequiredPrincipal User user, @RequestBody ExpenseCreateRequest request) {
        Long expenseId = expenseService.createExpense(user, request);
        return ResponseEntity.created(URI.create("/expenses/" + expenseId)).build();
    }

    @GetMapping("/api/users/{userId}/expenses/range")
    public ResponseEntity<MonthlyExpenseResponse> readMonthlyExpenses(@PathVariable("userId") Long userId,
                                                                      @RequestParam("from") LocalDate from,
                                                                      @RequestParam("to") LocalDate to) {
        MonthlyExpenseResponse monthlyExpenseResponse = expenseService.readMonthlyExpenses(userId, from, to);
        return ResponseEntity.ok(monthlyExpenseResponse);
    }

    @GetMapping("/api/users/{userId}/expenses")
    public ResponseEntity<TodayExpenses> readTodayExpenses(
            @PathVariable("userId") Long userId,
            @RequestParam("date") LocalDate date) {
        TodayExpenses todayExpenses = expenseService.readTodayExpenses(userId, date);
        return ResponseEntity.ok(todayExpenses);
    }

    @GetMapping("/api/community/expenses")
    public ResponseEntity<List<CommunityExpenseResponse>> readCommunityExpenses(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        List<CommunityExpenseResponse> communityExpenseResponses = expenseService.readCommunityExpenses(size, page);
        return ResponseEntity.ok(communityExpenseResponses);
    }

    @GetMapping("/api/community/expenses/emojis")
    public ResponseEntity<List<CommunityExpenseResponse>> readCommunityExpensesByReactedEmoji(@AuthRequiredPrincipal User user) {
        List<CommunityExpenseResponse> communityExpenseResponses = expenseService.readCommunityExpensesByReactedEmoji(user);
        return ResponseEntity.ok(communityExpenseResponses);
    }

    @PostMapping("/api/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("expenseId") Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}
