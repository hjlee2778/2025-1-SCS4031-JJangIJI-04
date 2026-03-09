package com.jjangiji.hankkimoa.user.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.user.domain.Bookmark;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.BookmarkRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public void createBookmark(Long userId, Long restaurantId) {
        if(bookmarkRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new HankkiMoaException(ExceptionCode.BOOKMARK_EXISTS);
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.RESTAURANT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.USER_NOT_FOUND));
        Bookmark bookmark = new Bookmark(user, restaurant);
        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long userId, Long restaurantId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(()-> new HankkiMoaException(ExceptionCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }
}
