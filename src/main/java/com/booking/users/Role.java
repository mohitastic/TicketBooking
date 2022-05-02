package com.booking.users;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN(Code.ADMIN),
    CUSTOMER(Code.CUSTOMER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Code {
        public static final String ADMIN = "ADMIN";
        public static final String CUSTOMER = "CUSTOMER";
    }
}
