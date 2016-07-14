package com.retailstore.dao;


import com.retailstore.dto.ProductDto;

import java.util.List;

/**
 * Created by kranthi on 6/23/2016.
 */
public interface IProductDao {

    List<ProductDto> getProductsByIds(List<Long> productIds);

    List<ProductDto> findAll();

    void delete(long id);

    void save(ProductDto ProductDto);

}
