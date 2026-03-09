package com.jjangiji.hankkimoa.common;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.config.CommonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerTest implements CommonTest {

    @DisplayName("핸들러 동작 성공")
    @Test
    void handleHankkiMoaException() {
        Assertions.assertThatThrownBy(() -> {
            throw new HankkiMoaException(ExceptionCode.INVALID_PARAMETER);
        }).isInstanceOf(HankkiMoaException.class);
    }
}
