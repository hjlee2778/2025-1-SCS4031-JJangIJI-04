package com.jjangiji.hankkimoa.user.service.dto;

import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import java.util.List;

public record UserMeResponse(
        Long id,
        String email,
        String nickname,
        String imageUrl,
        LoginType loginType,
        Role role,
        List<CategoryResponse> categories
) {

}
