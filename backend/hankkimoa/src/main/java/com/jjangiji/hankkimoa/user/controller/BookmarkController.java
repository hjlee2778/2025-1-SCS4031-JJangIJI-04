package com.jjangiji.hankkimoa.user.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<String> createBookmark(@AuthRequiredPrincipal User user, @PathVariable Long restaurantId) {
        bookmarkService.createBookmark(user.getId(), restaurantId);
        return ResponseEntity.ok("즐겨찾기에 추가되었습니다.");
    }

    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<String> deleteBookmark( @AuthRequiredPrincipal User user, @PathVariable Long restaurantId) {
        bookmarkService.deleteBookmark(user.getId(), restaurantId);
        return ResponseEntity.ok("즐겨찾기가 삭제되었습니다.");
    }
}
