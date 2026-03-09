package com.jjangiji.hankkimoa.restaurant.service.dto.reqeust;

import java.time.LocalTime;

public record OpeningHoursDetailRequest(LocalTime startTime, LocalTime endTime,
                                        LocalTime breakStartTime, LocalTime breakEndTime,
                                        LocalTime lastOrderTime) {
}
