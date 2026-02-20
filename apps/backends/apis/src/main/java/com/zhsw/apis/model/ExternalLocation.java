package com.zhsw.apis.model;

import lombok.Data;

@Data
public class ExternalLocation {
    private String postalCode;
    private String name;
    private Canton canton;
}
