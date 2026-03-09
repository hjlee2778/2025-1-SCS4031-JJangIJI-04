package com.jjangiji.hankkimoa.restaurant.domain;

import com.jjangiji.hankkimoa.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "식당이 NULL일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @NotNull(message = "메뉴이름이 NULL일 수 없습니다.")
    private String name;

    private Integer price;

    @Column(length = 1000)
    private String imageUrl;

    private boolean isMain;

    private String introduce;

    public Menu(Restaurant restaurant, String name, Integer price, String imageUrl, boolean isMain, String introduce) {
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.introduce = introduce;
    }

    public Menu(Long id, Restaurant restaurant, String name, Integer price, String imageUrl, boolean isMain, String introduce) {
        this(restaurant, name, price, imageUrl, isMain, introduce);
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Menu menu = (Menu) object;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
