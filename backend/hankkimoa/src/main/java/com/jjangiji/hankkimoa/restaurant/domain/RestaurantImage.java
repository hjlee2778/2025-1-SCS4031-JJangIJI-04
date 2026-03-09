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
public class RestaurantImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "식당이 NULL일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @NotNull(message = "이미지 주소가 NULL일 수 없습니다.")
    @Column(length = 1000)
    private String imageUrl;

    public RestaurantImage(Restaurant restaurant, String imageUrl) {
        this.restaurant = restaurant;
        this.imageUrl = imageUrl;
    }

    public RestaurantImage(Long id, Restaurant restaurant, String imageUrl) {
        this(restaurant, imageUrl);
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
        RestaurantImage that = (RestaurantImage) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
