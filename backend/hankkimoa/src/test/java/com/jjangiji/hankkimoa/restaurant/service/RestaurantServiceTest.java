package com.jjangiji.hankkimoa.restaurant.service;

import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.OpeningHour;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.restaurant.repository.OpeningHoursRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantSimpleResponse;
import com.jjangiji.hankkimoa.user.domain.Bookmark;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.BookmarkRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest extends IntegrationTest {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OpeningHoursRepository openingHoursRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;

    private Category category;
    private User user;
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
        category = categoryRepository.save(new Category(CategoryType.한식));
    }

    @DisplayName("식당 조회")
    @Test
    void readRestaurant() {
        // given
        Restaurant restaurant = restaurantRepository.save(new Restaurant(category, "한끼식당1", "100", 10000, address));

        // when
        RestaurantResponse result = restaurantService.readRestaurant(user, restaurant.getId());

        // then
        Assertions.assertThat(result.id()).isEqualTo(restaurant.getId());
    }

    @DisplayName("식당 조회 : 영업시간 정렬")
    @Test
    void readRestaurantWithSortedOpeningHours() {
        // given
        // todo 테스트 방법 다시 고민
        Restaurant restaurant = restaurantRepository.save(new Restaurant(category, "한끼식당1", "100", 10000, address));
        OpeningHour openingHour1 = new OpeningHour(restaurant, "수", LocalTime.of(10, 0), LocalTime.of(20, 0), null, null, null);
        OpeningHour openingHour2 = new OpeningHour(restaurant, "월", LocalTime.of(10, 0), LocalTime.of(20, 0), null, null, null);
        openingHoursRepository.saveAll(List.of(openingHour1, openingHour2));

        // when
        RestaurantResponse result = restaurantService.readRestaurant(user, restaurant.getId());

        // then
        Assertions.assertThat(result.openingHour()).containsExactly("월 10:00 - 20:00", "수 10:00 - 20:00");
    }

    @DisplayName("북마크된 식당 조회 성공")
    @Test
    void readBookmarkedRestaurants() {
        // given
        Restaurant restaurant = restaurantRepository.save(new Restaurant(category, "한끼식당1", "100", 10000, address));
        Bookmark bookmark = bookmarkRepository.save(new Bookmark(user, restaurant));

        // when
        List<RestaurantSimpleResponse> result = restaurantService.readBookmarkedRestaurants(user);

        // then
        Assertions.assertThat(result.get(0).id()).isEqualTo(restaurant.getId());
    }
}
