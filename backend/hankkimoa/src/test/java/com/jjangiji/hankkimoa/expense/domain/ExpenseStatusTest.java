package com.jjangiji.hankkimoa.expense.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpenseStatusTest {

    @DisplayName("데일리 지출 상태 변환 성공 : GOOD")
    @Test
    void convertGOOD() {
        // 예산: 10000원, 기간: 10일 → 하루 예산: 10000
        ExpenseStatus status = ExpenseStatus.convert(10_000, 8500);

        Assertions.assertThat(status).isEqualTo(ExpenseStatus.GOOD);
    }

    @DisplayName("데일리 지출 상태 변환 성공 : BAD")
    @Test
    void convertBAD() {
        ExpenseStatus status = ExpenseStatus.convert(10_000, 11_000);

        Assertions.assertThat(status).isEqualTo(ExpenseStatus.BAD);
    }
}
