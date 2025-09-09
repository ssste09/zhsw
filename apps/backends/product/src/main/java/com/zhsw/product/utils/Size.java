package com.zhsw.product.utils;

public enum Size {
    EU_25,
    EU_26,
    EU_27,
    EU_28,
    EU_29,
    EU_30,
    EU_31,
    EU_32,
    EU_33,
    EU_34,
    EU_35,
    EU_36,
    EU_37,
    EU_38,
    EU_39,
    EU_40,
    EU_41,
    EU_42,
    EU_43,
    EU_44,
    EU_45;

    public static Size fromOpenApi(String openApiName) {
        return switch (openApiName) {
            case "_25" -> EU_25;
            case "_26" -> EU_26;
            case "_27" -> EU_27;
            case "_28" -> EU_28;
            case "_29" -> EU_29;
            case "_30" -> EU_30;
            case "_31" -> EU_31;
            case "_32" -> EU_32;
            case "_33" -> EU_33;
            case "_34" -> EU_34;
            case "_35" -> EU_35;
            case "_36" -> EU_36;
            case "_37" -> EU_37;
            case "_38" -> EU_38;
            case "_39" -> EU_39;
            case "_40" -> EU_40;
            case "_41" -> EU_41;
            case "_42" -> EU_42;
            case "_43" -> EU_43;
            case "_44" -> EU_44;
            case "_45" -> EU_45;
            default -> throw new IllegalArgumentException("Unknown size: " + openApiName);
        };
    }
}
