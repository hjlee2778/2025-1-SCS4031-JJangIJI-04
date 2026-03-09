package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.user.domain.User;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpenseEmojis {

    private final Map<Integer, List<User>> expenseEmojisMap = new LinkedHashMap<>();

    public ExpenseEmojis(List<ExpenseEmoji> expenseEmojis) {
        for (ExpenseEmoji expenseEmoji : expenseEmojis) {
            expenseEmojisMap.computeIfAbsent(
                    expenseEmoji.getEmojiId(),
                    k -> new ArrayList<>()).add(expenseEmoji.getUser());
        }
    }

    public List<Integer> getEmojiIds() {
        return new ArrayList<>(expenseEmojisMap.keySet());
    }

    public List<Long> getUserIds(Integer emojiId) {
        return expenseEmojisMap.get(emojiId)
                .stream()
                .map(User::getId)
                .toList();
    }
}
