package com.jjangiji.hankkimoa.restaurant.util;

import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static com.jjangiji.hankkimoa.common.exception.ExceptionCode.RESTAURANT_DAYOFWEEK_INTERNAL_EXCEPTION;

public class DayOfWeekMapper {

    private static final Map<Pattern, DayOfWeek> DAYOFWEEK_MAP = Map.of(
            Pattern.compile("일.*"), DayOfWeek.SUNDAY,
            Pattern.compile("월.*"), DayOfWeek.MONDAY,
            Pattern.compile("화.*"), DayOfWeek.TUESDAY,
            Pattern.compile("수.*"), DayOfWeek.WEDNESDAY,
            Pattern.compile("목.*"), DayOfWeek.THURSDAY,
            Pattern.compile("금.*"), DayOfWeek.FRIDAY,
            Pattern.compile("토.*"), DayOfWeek.SATURDAY
    );

    public static DayOfWeek from(String koreanDayOfWeek) {
        return DAYOFWEEK_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(koreanDayOfWeek).matches())
                .findAny()
                .map(Entry::getValue)
                .orElseThrow(() -> new HankkiMoaException(RESTAURANT_DAYOFWEEK_INTERNAL_EXCEPTION));
    }
}
