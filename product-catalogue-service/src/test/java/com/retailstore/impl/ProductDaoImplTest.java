package com.retailstore.impl;

import com.retailstore.dao.impl.ProductDaoImpl;
import com.retailstore.dto.ProductDto;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranthi on 23-06-2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoImplTest {
    private static EmbeddedDatabase db;
    private static ProductDaoImpl productDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-db.sql")
                .addScript("insert-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(db);
        productDao = new ProductDaoImpl(jdbcTemplate);
    }

    @Test
    public void test2GetProductsByIds() {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        productIds.add(2L);
        List<ProductDto> productDtos = productDao.getProductsByIds(productIds);
        Assert.assertTrue(productDtos.size() == 2);
    }

    @Test
    public void test1FindAll() {
        List<ProductDto> productDtos = productDao.findAll();
        Assert.assertTrue(productDtos.size() == 4);

    }

    @Test
    public void test3Delete() {
        productDao.delete(1L);
        List<ProductDto> productDtos = productDao.findAll();
        Assert.assertTrue(productDtos.size() == 3);
    }

    @Test
    public void test4Save() {
        ProductDto productDto = new ProductDto();
        productDto.setId(5L);
        productDto.setCategory("A");
        productDto.setName("SWIFT");
        productDto.setCost(new BigDecimal(100.0));
        productDao.save(productDto);
        List<ProductDto> productDtos = productDao.findAll();
        Assert.assertTrue(productDtos.size() == 4);

    }
}
