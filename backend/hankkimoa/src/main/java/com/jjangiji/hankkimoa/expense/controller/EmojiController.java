package com.jjangiji.hankkimoa.expense.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.expense.service.EmojiService;
import com.jjangiji.hankkimoa.expense.service.dto.request.EmojiCreateRequest;
import com.jjangiji.hankkimoa.expense.service.dto.request.EmojiDeleteRequest;
import com.jjangiji.hankkimoa.expense.service.dto.response.EmojiCreateResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmojiController {

    private final EmojiService emojiService;

    @PostMapping("/api/expenses/emojis")
    public ResponseEntity<EmojiCreateResponse> createEmoji(@AuthRequiredPrincipal User user,
                                                           @RequestBody EmojiCreateRequest request) {
        EmojiCreateResponse response = emojiService.createEmoji(user, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/expenses/emojis")
    public ResponseEntity<Void> deleteEmoji(@RequestBody EmojiDeleteRequest request) {
        emojiService.deleteExpense(request);
        return ResponseEntity.noContent().build();
    }
}
