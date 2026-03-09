package com.jjangiji.hankkimoa.user.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.domain.UserCategory;
import com.jjangiji.hankkimoa.user.repository.UserCategoryRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import com.jjangiji.hankkimoa.user.service.dto.CategoryUpdateRequest;
import com.jjangiji.hankkimoa.user.service.dto.NicknameUpdateRequest;
import com.jjangiji.hankkimoa.user.service.dto.UserMeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

class UserServiceTest extends IntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserCategoryRepository userCategoryRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
    }

    @DisplayName("사용자 카테고리 업데이트 성공")
    @Test
    void updateCategories() {
        // given
        Category category1 = categoryRepository.save(new Category(CategoryType.한식));
        Category category2 = categoryRepository.save(new Category(CategoryType.양식));
        userCategoryRepository.save(new UserCategory(user, category1));

        // when
        CategoryUpdateRequest request = new CategoryUpdateRequest(List.of(category2.getId()));
        userService.updateCategories(user, request);

        // then
        UserMeResponse userMeResponse = userService.readMyInfo(user);
        Integer userCategoryId = userMeResponse.categories().get(0).categoryId();

        Assertions.assertThat(userMeResponse.categories()).hasSize(1);
        Assertions.assertThat(userCategoryId).isEqualTo(category2.getId());
    }

    @DisplayName("사용자 카테고리 업데이트 실패 : 카테고리가 존재하지 않는 경우")
    @Test
    void failWhenCategoryNotExist() {
        // given
        CategoryUpdateRequest request = new CategoryUpdateRequest(List.of(0));

        // when & then
        Assertions.assertThatThrownBy(() -> userService.updateCategories(user, request))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @DisplayName("닉네임 업데이트 성공")
    @Test
    void updateNickname() {
        // given
        String updateNickname = "짱이지";
        NicknameUpdateRequest request = new NicknameUpdateRequest(updateNickname);

        // when
        userService.updateNickname(user, request);

        // then
        String updatedNickname = userService.readMyInfo(user).nickname();
        Assertions.assertThat(updatedNickname).isEqualTo(updateNickname);
    }
}
