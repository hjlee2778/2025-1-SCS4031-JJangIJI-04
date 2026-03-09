package com.jjangiji.hankkimoa.restaurant.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Address {

    private double latitude;

    private double longitude;

    @NotNull(message = "주소가 NULL일 수 없습니다.")
    private String streetAddress;

    public Address(double latitude, double longitude, String streetAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetAddress = streetAddress;
    }
}
