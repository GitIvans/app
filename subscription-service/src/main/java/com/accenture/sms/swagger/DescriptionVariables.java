package com.accenture.sms.swagger;

public class DescriptionVariables {

    public static final String SUBSCRIPTION_INFO_CONTROLLER = "Subscription Info Controller";
    public static final String SUBSCRIPTION_INFO_MODEL = "Subscription Info Model";

    // Common validation messages
    public static final String NON_NULL_MAX_LONG = "Required non-null value. Starting from 1 to Long.MAX_VALUE";
    public static final String MAX_LONG_RANGE = "range[1, 9223372036854775807]";
    public static final String MODEL_ID_MIN = "Id must be bigger than 0";
    public static final String MODEL_ID_MAX = "Id must be less than 9,223,372,036,854,775,808";

    // String validation messages
    public static final String CONTAIN_ALPHANUMERIC_CHAR_MESSAGE = "This field may contain only letters, numbers and spaces";
    public static final String STRING_PATTERN = "^[a-zA-Z0-9\\s]+$";

}
