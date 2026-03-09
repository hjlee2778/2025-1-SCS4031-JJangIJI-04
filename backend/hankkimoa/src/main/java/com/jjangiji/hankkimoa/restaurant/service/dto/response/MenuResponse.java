package com.jjangiji.hankkimoa.restaurant.service.dto.response;

import com.jjangiji.hankkimoa.restaurant.domain.Menu;

public record MenuResponse(String name,
                           String introduce,
                           Integer price,
                           String imgUrl,
                           boolean main) {

    public MenuResponse(Menu menu) {
        this(
            menu.getName(),
            menu.getIntroduce(),
            menu.getPrice(),
            menu.getImageUrl(),
            menu.isMain()
        );
    }
}
