package com.jjangiji.hankkimoa.auth;

import com.jjangiji.hankkimoa.auth.service.AuthService;
import com.jjangiji.hankkimoa.auth.service.dto.request.SignupRequest;
import com.jjangiji.hankkimoa.auth.service.dto.response.SignupResponse;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.IntegrationTest;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.UserCategoryRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

class AuthServiceTest extends IntegrationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserCategoryRepository userCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER));
    }

    @AfterEach
    void tearDown() {
        userCategoryRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 실패 : 카테고리가 존재하지 않는 경우")
    @Test
    void failWhenCategoryNotFound() {
        // given
        SignupRequest request = new SignupRequest("한끼모아", List.of(1));

        // when
        Assertions.assertThatCode(() ->  authService.signup(user, request))
                        .isInstanceOf(HankkiMoaException.class)
                        .hasMessage(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @DisplayName("회원가입 성공 : 유저 닉네임 업데이트 성공")
    @Test
    void updateNickname() {
        // given
        Category category = categoryRepository.save(new Category(CategoryType.한식));
        SignupRequest request = new SignupRequest("한끼모아", List.of(category.getId()));

        // when
        SignupResponse result = authService.signup(user, request);

        // then
        Assertions.assertThat(result.nickname()).isEqualTo(request.nickname());
    }

    @DisplayName("회원가입 성공 : 유저 카테고리 추가 성공")
    @Test
    void createCategories() {
        // given
        Category category1 = categoryRepository.save(new Category(CategoryType.한식));
        Category category2 = categoryRepository.save(new Category(CategoryType.양식));
        SignupRequest request = new SignupRequest("한끼모아", List.of(category1.getId(), category2.getId()));

        // when
        SignupResponse result = authService.signup(user, request);

        // then
        Assertions.assertThat(result.categories()).isEqualTo(request.categories());
    }
}
