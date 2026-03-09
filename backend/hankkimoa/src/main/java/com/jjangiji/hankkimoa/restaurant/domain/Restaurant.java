package com.jjangiji.hankkimoa.restaurant.domain;

import com.jjangiji.hankkimoa.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "카테고리는 NULL일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @NotNull(message = "이름이 NULL일 수 없습니다.")
    private String name;

    @NotNull(message = "고유ID는 NULL일 수 없습니다.")
    @Column(unique = true)
    private String uniqueId;

    @Embedded
    private Address address;

    @NotNull(message = "메뉴 평균 가격이 NULL일 수 없습니다.")
    private Integer menuAverage;

    public Restaurant(Category category, String name, String uniqueId, Integer menuAverage, Address address) {
        this.category = category;
        this.name = name;
        this.uniqueId = uniqueId;
        this.menuAverage = menuAverage;
        this.address = address;
    }

    public Restaurant(Long id, Category category, String name, String uniqueId, Integer menuAverage, Address address) {
        this(category, name, uniqueId, menuAverage, address);
        this.id = id;
    }

    public String getCategoryName() {
        return category.getName();
    }

    public String getStreetAddress() {
        return address.getStreetAddress();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Restaurant that = (Restaurant) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
