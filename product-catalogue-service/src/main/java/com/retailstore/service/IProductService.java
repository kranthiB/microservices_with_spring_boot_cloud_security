package com.retailstore.service;

import com.retailstore.dto.ProductDto;

import java.util.List;

/**
 * Created by kranthi on 6/23/2016.
 */
public interface IProductService {

    void addProduct(ProductDto productVo);

    List<ProductDto> getProducts();

    void deleteProduct(Long id);

    List<ProductDto> getProductsByIds(long[] productIds);
}
