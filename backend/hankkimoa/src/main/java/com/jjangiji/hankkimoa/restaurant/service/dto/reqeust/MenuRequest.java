package com.jjangiji.hankkimoa.restaurant.service.dto.reqeust;

public record MenuRequest(boolean isMain, String name,
                          String introduce, Integer price, String imgUrl) {
}
