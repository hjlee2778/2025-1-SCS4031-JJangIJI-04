package com.jjangiji.hankkimoa.restaurant.domain;

import com.jjangiji.hankkimoa.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class OpeningHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "식당이 NULL일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private String dayOfWeek;

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalTime breakStartTime;

    private LocalTime breakEndTime;

    private LocalTime lastOrderTime;

    public OpeningHour(Restaurant restaurant, String dayOfWeek, LocalTime openTime, LocalTime closeTime,
                       LocalTime breakStartTime, LocalTime breakEndTime, LocalTime lastOrderTime) {
        this.restaurant = restaurant;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.lastOrderTime = lastOrderTime;
    }

    public OpeningHour(Long id, Restaurant restaurant, String dayOfWeek, LocalTime openTime, LocalTime closeTime,
                       LocalTime breakStartTime, LocalTime breakEndTime, LocalTime lastOrderTime) {
        this(restaurant, dayOfWeek, openTime, closeTime, breakStartTime, breakEndTime, lastOrderTime);
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
        OpeningHour that = (OpeningHour) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
