package com.jjangiji.hankkimoa.auth.service;

import com.jjangiji.hankkimoa.auth.service.dto.request.OauthLoginRequest;
import com.jjangiji.hankkimoa.auth.service.dto.request.SignupRequest;
import com.jjangiji.hankkimoa.auth.service.dto.response.AuthTokenResponse;
import com.jjangiji.hankkimoa.auth.service.dto.response.OauthInfoApiResponse;
import com.jjangiji.hankkimoa.auth.service.dto.response.SignupResponse;
import com.jjangiji.hankkimoa.auth.service.jwt.JwtTokenProvider;
import com.jjangiji.hankkimoa.auth.service.jwt.JwtTokenResolver;
import com.jjangiji.hankkimoa.auth.service.oauth.OauthClient;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.repository.CategoryRepository;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.domain.UserCategory;
import com.jjangiji.hankkimoa.user.repository.UserCategoryRepository;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final OauthClient oauthClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenResolver jwtTokenResolver;

    @Transactional
    public AuthTokenResponse oauthLogin(OauthLoginRequest request) {
        OauthInfoApiResponse oauthInfo = oauthClient.requestOauthInfo(request);

        User user = userRepository.findByEmail(oauthInfo.kakao_account().email())
                .orElseGet(() -> userRepository.save(oauthInfo.toUserEntity()));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        return new AuthTokenResponse(user.getNickname(), user.getImageUrl(), accessToken, refreshToken);
    }

    @Transactional
    public SignupResponse signup(User user, SignupRequest request) {
        user.updateNickname(request.nickname());
        List<Category> categories = readCategories(request.categories());
        List<UserCategory> userCategories = categories
                .stream()
                .map(category -> new UserCategory(user, category))
                .toList();
        userCategoryRepository.saveAll(userCategories);

        return new SignupResponse(user.getNickname(), user.getImageUrl(), user.isExpenseOpen(), request.categories());
    }

    private List<Category> readCategories(List<Integer> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new HankkiMoaException(ExceptionCode.CATEGORY_NOT_FOUND);
        }

        List<Category> result = categoryRepository.findAllById(categories);
        if (result.size() != categories.size()) {
            throw new HankkiMoaException(ExceptionCode.CATEGORY_NOT_FOUND);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public String refreshToken(String refreshToken) {
        AuthUser authUser = jwtTokenResolver.resolveRefreshToken(refreshToken);
        User user = readUser(authUser.id());
        return jwtTokenProvider.createAccessToken(user);
    }

    private User readUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.USER_NOT_FOUND));
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        AuthUser accessAuthUser = jwtTokenResolver.resolveAccessToken(accessToken);
        AuthUser refreshAuthUser = jwtTokenResolver.resolveRefreshToken(refreshToken);
        if (!accessAuthUser.id().equals(refreshAuthUser.id())) {
            throw new HankkiMoaException(ExceptionCode.AUTHENTICATION_TOKEN_USER_MISMATCH);
        }
    }
}
