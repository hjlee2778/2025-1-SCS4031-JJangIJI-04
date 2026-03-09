package com.jjangiji.hankkimoa.expense.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record EmojiDeleteRequest(@NotNull(message = "지출 ID를 입력해주세요.") Long expenseId,
                                 @NotNull(message = "이모지 ID를 입력해주세요.") Integer emojiId) {
}
