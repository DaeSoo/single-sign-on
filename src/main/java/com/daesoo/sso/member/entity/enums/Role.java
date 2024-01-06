package com.daesoo.sso.member.entity.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "guest"),
    USER("ROLE_USER", "user"),
    ADMIN("ROLE_ADMIN", "admin");

    private final String key;
    private final String title;
}
