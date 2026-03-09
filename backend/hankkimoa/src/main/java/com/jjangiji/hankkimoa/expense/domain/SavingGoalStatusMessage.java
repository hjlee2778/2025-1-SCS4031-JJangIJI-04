package com.jjangiji.hankkimoa.expense.domain;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum SavingGoalStatusMessage {

    SUCCESS("잘 절약하고 있어요! 앞으로도 화이팅!", 60, 100),
    ENCOURAGE("조금 만 더 노력해볼까요? 오늘도 힘내세요!", 30, 60),
    WARNING("절약 금액이 얼마 남지 않았어요! 오늘은 가성비 맛집을 찾아보는게 어떨까요?", 0, 30),
    FAIL("이번 주는 달성에 실패했습니다. 다음 주에는 성공하기를 바래요!", -1, 0);

    private final String message;
    private final int minPercentage;
    private final int maxPercentage;

    SavingGoalStatusMessage(String message, int minPercentage, int maxPercentage) {
        this.message = message;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
    }

    public static SavingGoalStatusMessage from(int usedPercentage) {
        return Arrays.stream(values())
                .filter(status -> status.minPercentage < usedPercentage && usedPercentage <= status.maxPercentage)
                .findFirst()
                .orElse(FAIL);
    }
}
