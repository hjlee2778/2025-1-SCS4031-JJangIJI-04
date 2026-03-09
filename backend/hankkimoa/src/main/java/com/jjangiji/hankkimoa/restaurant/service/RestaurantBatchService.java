package com.jjangiji.hankkimoa.restaurant.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Menu;
import com.jjangiji.hankkimoa.restaurant.domain.OpeningHour;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.restaurant.domain.RestaurantImage;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.restaurant.repository.MenuRepository;
import com.jjangiji.hankkimoa.restaurant.repository.OpeningHoursRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantImageRepository;
import com.jjangiji.hankkimoa.restaurant.repository.RestaurantRepository;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RestaurantCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RestaurantBatchService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createRestaurants(List<RestaurantCreateRequest> requests) {
        Set<String> uniqueIds = restaurantRepository.findAllUniqueId();
        List<Category> categories = categoryRepository.findAll();

        for (RestaurantCreateRequest request : requests) {
            if (request.menus() == null) continue;
            if (request.id() == null || uniqueIds.contains(request.id())) continue;

            Restaurant restaurant = saveRestaurant(request, categories);
            uniqueIds.add(restaurant.getUniqueId());
            saveRestaurantImages(restaurant, request);
            saveOpeningHours(restaurant, request);
            saveMenus(restaurant, request);
        }
    }

    private Restaurant saveRestaurant(RestaurantCreateRequest request, List<Category> categories) {
        Category category = null;
        Address address = null;
        if (request.category() != null) category = categories.stream()
                .filter(c -> c.getCategoryType().equals(CategoryType.findByKeyword(request.category())))
                .findAny()
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.CATEGORY_NOT_FOUND));
        if (request.address() != null) address = new Address(0, 0, request.address());

        Restaurant restaurant = new Restaurant(category, request.name(), request.id(), request.menu_average(), address);
        return restaurantRepository.save(restaurant);
    }

    private void saveRestaurantImages(Restaurant restaurant, RestaurantCreateRequest request) {
        if (request.images() == null) return;

        List<RestaurantImage> restaurantImages = request.images()
                .stream()
                .map(image -> new RestaurantImage(restaurant, image))
                .toList();
        restaurantImageRepository.saveAll(restaurantImages);
    }

    private void saveOpeningHours(Restaurant restaurant, RestaurantCreateRequest request) {
        if (request.openingHours() == null) return;

        List<OpeningHour> openingHours = request.openingHours().stream()
                .map(openingHour -> new OpeningHour(restaurant, openingHour.dayOfWeek(),
                        openingHour.hours().startTime(), openingHour.hours().endTime(),
                        openingHour.hours().breakStartTime(), openingHour.hours().breakEndTime(),
                        openingHour.hours().lastOrderTime()))
                .toList();
        openingHoursRepository.saveAll(openingHours);
    }

    private void saveMenus(Restaurant restaurant, RestaurantCreateRequest request) {
        if (request.menus() == null) return;

        List<Menu> menus = request.menus().stream()
                .map(menu -> new Menu(restaurant, menu.name(),
                        menu.price(), menu.imgUrl(),
                        menu.isMain(), menu.introduce()))
                .toList();
        menuRepository.saveAll(menus);
    }
}
