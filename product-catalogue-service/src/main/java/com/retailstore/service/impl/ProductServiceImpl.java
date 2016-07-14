package com.retailstore.service.impl;

import com.google.common.primitives.Longs;
import com.retailstore.dao.IProductDao;
import com.retailstore.dto.ProductDto;
import com.retailstore.service.IProductService;

import java.util.List;

/**
 * Created by kranthi on 6/23/2016.
 */
public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao;

    public ProductServiceImpl(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void addProduct(ProductDto productVo) {
        productDao.save(productVo);
    }

    @Override
    public List<ProductDto> getProducts() {
        return productDao.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        productDao.delete(id);
    }

    @Override
    public List<ProductDto> getProductsByIds(long[] productIds) {
        return productDao.getProductsByIds(Longs.asList(productIds));
    }
}
