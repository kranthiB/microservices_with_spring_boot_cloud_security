package com.retailstore.util;

/**
 * Created by user on 10-07-2016.
 */
public enum ServiceName {

    PRODUCT_CATALOGUE_SERVICE("productCatalogueService");

    private String name;

    ServiceName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
