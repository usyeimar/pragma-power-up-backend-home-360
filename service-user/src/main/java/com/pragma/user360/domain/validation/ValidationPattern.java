package com.pragma.user360.domain.validation;

import java.util.regex.Pattern;

enum ValidationPattern {
    NUMERIC("^[0-9]+$"),
    PHONE("^\\+?[0-9]{7,12}$"),
    EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final Pattern pattern;

    ValidationPattern(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }
}