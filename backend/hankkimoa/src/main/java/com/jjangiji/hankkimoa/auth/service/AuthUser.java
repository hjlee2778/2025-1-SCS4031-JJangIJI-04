package com.jjangiji.hankkimoa.auth.service;

import jakarta.validation.constraints.NotNull;

public record AuthUser(@NotNull Long id) {

    public static AuthUser from(Long id) {
        return new AuthUser(id);
    }
}
