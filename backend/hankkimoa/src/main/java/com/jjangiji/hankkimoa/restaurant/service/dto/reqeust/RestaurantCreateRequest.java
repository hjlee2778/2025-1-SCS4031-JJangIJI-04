package com.jjangiji.hankkimoa.restaurant.service.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RestaurantCreateRequest(@NotNull(message = "식당 ID를 입력해주세요.") String id,
                                      @NotNull(message = "식당 이름을 입력해주세요.") String name,
                                      @NotNull(message = "카테고리를 입력해주세요.")String category,
                                      @NotNull(message = "주소를 입력해주세요.")String address,
                                      @NotNull(message = "메뉴 평균을 입력해주세요.")Integer menu_average,
                                      @NotNull(message = "이미지를 입력해주세요.") List<String> images,
                                      @NotNull(message = "영업시간을 입력해주세요.") List<OpeningHoursRequest> openingHours,
                                      @NotNull(message = "메뉴 상세 정보를 입력해주세요.") List<MenuRequest> menus) {
}
