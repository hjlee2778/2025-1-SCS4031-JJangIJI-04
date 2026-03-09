package com.jjangiji.hankkimoa.user.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.domain.UserCategory;
import com.jjangiji.hankkimoa.user.repository.UserCategoryRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import com.jjangiji.hankkimoa.user.service.dto.CategoryResponse;
import com.jjangiji.hankkimoa.user.service.dto.CategoryUpdateRequest;
import com.jjangiji.hankkimoa.user.service.dto.NicknameUpdateRequest;
import com.jjangiji.hankkimoa.user.service.dto.UserMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserCategoryRepository userCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserMeResponse readMyInfo(User user) {
        List<CategoryResponse> categories = userCategoryRepository.findAllByUser(user).stream()
                .map(userCategory -> new CategoryResponse(userCategory.getCategory().getId(), userCategory.getCategoryName()))
                .toList();

        return new UserMeResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getImageUrl(),
                user.getLoginType(),
                user.getRole(),
                categories);
    }

    @Transactional
    public void updateCategories(User user, CategoryUpdateRequest request) {
        List<Category> categories = categoryRepository.findAllByIdIn(request.categories());
        validateCategoryExist(request.categories(), categories);

        userCategoryRepository.deleteAllByUserId(user.getId());

        List<UserCategory> userCategories = categories.stream()
                .map(category -> new UserCategory(user, category))
                .toList();
        userCategoryRepository.saveAll(userCategories);
    }

    private void validateCategoryExist(List<Integer> categoryIds, List<Category> categories) {
        if (categoryIds.size() != categories.size()) {
            throw new HankkiMoaException(ExceptionCode.CATEGORY_NOT_FOUND);
        }
    }

    @Transactional
    public void updateNickname(User user, NicknameUpdateRequest request) {
        user.updateNickname(request.nickname());
        userRepository.save(user);
    }
}
