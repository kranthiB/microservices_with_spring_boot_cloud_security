package com.retailstore.impl;

import com.retailstore.dao.IProductDao;
import com.retailstore.dto.ProductDto;
import com.retailstore.service.IProductService;
import com.retailstore.service.impl.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranthi on 23-06-2016.
 */
public class ProductServiceImplTest {

    @Mock
    private IProductDao productDao;
    private IProductService productService;

    private List<ProductDto> productDtos;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productDao);
        productDtos = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("BMW");
        productDto.setCategory("A");
        productDto.setCost(new BigDecimal(100.0));
        productDtos.add(productDto);
        productDto = new ProductDto();
        productDto.setId(2L);
        productDto.setName("IPHONE");
        productDto.setCategory("B");
        productDto.setCost(new BigDecimal(100.0));
        productDtos.add(productDto);
    }

    @Test
    public void testAddProduct() {
        Mockito.doNothing().when(productDao).save(Mockito.any(ProductDto.class));
        ProductDto productDto = new ProductDto();
        productDto.setId(100L);
        productDto.setName("Name");
        productDto.setCategory("D");
        productDto.setCost(new BigDecimal(100.0));
        productService.addProduct(productDto);
        Mockito.verify(productDao).save(productDto);
    }

    @Test
    public void testGetProducts() {
        Mockito.doReturn(productDtos).when(productDao).findAll();
        List<ProductDto> productDtoList = productService.getProducts();
        Assert.assertTrue(productDtoList.size() == productDtos.size());
    }

    @Test
    public void testDeleteProduct() {
        Mockito.doNothing().when(productDao).delete(Mockito.anyLong());
        long id = 1L;
        productService.deleteProduct(id);
        Mockito.verify(productDao).delete(id);
    }

    @Test
    public void testGetProductsByIds() {
        Mockito.doReturn(productDtos).when(productDao).getProductsByIds(Mockito.anyListOf(Long.class));
        long[] productIds = new long[2];
        productIds[0] = 1L;
        productIds[1] = 2L;
        List<ProductDto> productDtoList = productService.getProductsByIds(productIds);
        Assert.assertTrue(productDtoList.size() == productDtos.size());
    }
}
