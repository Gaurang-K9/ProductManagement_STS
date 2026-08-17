package com.demo.constants;

public final class RegexConstants {

    private RegexConstants() {}

    public static final String EMAIL = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    public static final String NO_SPACES = "^\\S+$";

    public static final String NAME_ONLY = "^[a-zA-Z]+$";

    public static final String MOBILE_NUMBER = "^[0-9]{10}$";
}
