package com.jjangiji.hankkimoa.expense.service.dto.response;

import java.util.List;

public record EmojiResponse(Integer emojiId, Integer count, List<Long> userIds) {
}
