package com.jjangiji.hankkimoa.expense.service.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExpenseSavingGoalCreateRequest(@NotNull(message = "예산 금액을 입력해주세요.") Integer budget,
                                             @NotNull(message = "시작일을 입력해주세요.")LocalDate startDate,
                                             @NotNull(message = "종료일을 입력해주세요.")LocalDate endDate) {
}
