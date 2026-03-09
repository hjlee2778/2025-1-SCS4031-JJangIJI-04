package com.jjangiji.hankkimoa.restaurant.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Menu;
import com.jjangiji.hankkimoa.restaurant.domain.OpeningHour;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.domain.RestaurantImage;
import com.jjangiji.hankkimoa.restaurant.repository.MenuRepository;
import com.jjangiji.hankkimoa.restaurant.repository.OpeningHoursRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantImageRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.MenuResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantSearchResponse;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RestaurantSimpleResponse;
import com.jjangiji.hankkimoa.restaurant.util.DayOfWeekMapper;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final MenuRepository menuRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public List<RestaurantSearchResponse> searchRestaurants(String keyword) {
        List<Restaurant> restaurants = restaurantRepository.findAllByKeyword(keyword);
        return restaurants.stream()
                .map(restaurant -> new RestaurantSearchResponse(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getStreetAddress()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantSimpleResponse> readRestaurants(User user, List<String> uniqueIds) {
        List<Restaurant> restaurants = restaurantRepository.findAllByUniqueIdIn(uniqueIds);
        return restaurants.stream()
                .map(restaurant -> {
                        boolean bookmarked = bookmarkRepository.existsByUserIdAndRestaurantId(user.getId(), restaurant.getId());
                        return toRestaurantSimpleResponse(restaurant, bookmarked);
                })
                .toList();
    }

    private RestaurantSimpleResponse toRestaurantSimpleResponse(Restaurant restaurant, boolean bookmarked) {
        Optional<OpeningHour> openingHour = readTodayOpeningHour(restaurant);
        String restaurantImage = restaurantImageRepository.findAllByRestaurantId(restaurant.getId())
                .stream()
                .map(RestaurantImage::getImageUrl)
                .findFirst()
                .orElse(null);

        return new RestaurantSimpleResponse(restaurant, restaurantImage, formatOpeningHour(openingHour), bookmarked);
    }

    private Optional<OpeningHour> readTodayOpeningHour(Restaurant restaurant) {
        DayOfWeek todayDayOfWeek = LocalDate.now().getDayOfWeek();

        return openingHoursRepository.findAllByRestaurantId(restaurant.getId()).stream()
                .filter(openingHour -> isMatchDayOfWeek(todayDayOfWeek, openingHour))
                .findAny();
    }

    private boolean isMatchDayOfWeek(DayOfWeek todayDayOfWeek, OpeningHour openingHour) {
        if (openingHour.getDayOfWeek().equals("매일")) return true;

        DayOfWeek dayOfWeek = DayOfWeekMapper.from(openingHour.getDayOfWeek());
        return todayDayOfWeek.equals(dayOfWeek);
    }

    private String formatOpeningHour(Optional<OpeningHour> openingHour) {
        return openingHour
                .map(oh -> String.format("%s %s - %s", oh.getDayOfWeek(), oh.getOpenTime(), oh.getCloseTime()))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse readRestaurant(User user, Long restaurantId) {
        Restaurant restaurant = readRestaurant(restaurantId);
        List<OpeningHour> openingHours = openingHoursRepository.findAllByRestaurantId(restaurant.getId());
        List<RestaurantImage> restaurantImages = restaurantImageRepository.findAllByRestaurantId(restaurant.getId());
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurant.getId());
        boolean bookmarked = bookmarkRepository.existsByUserIdAndRestaurantId(user.getId(), restaurant.getId());

        return toRestaurantResponse(restaurant, openingHours, restaurantImages, menus, bookmarked);
    }

    private RestaurantResponse toRestaurantResponse(Restaurant restaurant,
                                                    List<OpeningHour> openingHours,
                                                    List<RestaurantImage> restaurantImages,
                                                    List<Menu> menus,
                                                    boolean bookmarked)
    {
        List<String> formattedOpeningHours = sortOpeningHours(openingHours)
                .stream()
                .map(openingHour -> formatOpeningHour(Optional.of(openingHour)))
                .toList();
        List<String> imageUrls = restaurantImages
                .stream()
                .map(RestaurantImage::getImageUrl)
                .toList();
        List<MenuResponse> menuResponses = menus
                .stream()
                .map(MenuResponse::new)
                .toList();

        return new RestaurantResponse(restaurant, imageUrls, formattedOpeningHours, menuResponses, bookmarked);
    }

    private Restaurant readRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.RESTAURANT_NOT_FOUND));
    }

    private List<OpeningHour> sortOpeningHours(List<OpeningHour> openingHours) {
        if (openingHours.size() == 1) return openingHours;

        return openingHours.stream()
                .sorted(Comparator.comparing(oh -> DayOfWeekMapper.from(oh.getDayOfWeek())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantSimpleResponse> readBookmarkedRestaurants(User user) {
        List<Restaurant> bookmarkedRestaurants = restaurantRepository.findAllBookmarkedRestaurantsOrderByCreatedAtDESC(user.getId());
        return bookmarkedRestaurants.stream()
                .map(restaurant -> toRestaurantSimpleResponse(restaurant, true ))
                .toList();
    }
}
