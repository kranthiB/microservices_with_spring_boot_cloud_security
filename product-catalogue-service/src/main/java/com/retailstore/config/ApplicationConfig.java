package com.retailstore.config;

import com.retailstore.controller.ProductController;
import com.retailstore.dao.IProductDao;
import com.retailstore.dao.impl.ProductDaoImpl;
import com.retailstore.service.IProductService;
import com.retailstore.service.impl.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by user on 10-07-2016.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ProductController productController(final IProductService productService) {
        return new ProductController(productService);
    }

    @Bean
    public IProductService productService(final IProductDao iProductDao) {
        return new ProductServiceImpl(iProductDao);
    }

    @Bean
    public IProductDao productDao(final JdbcTemplate jdbcTemplate) {
        return new ProductDaoImpl(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase embeddedDatabase = embeddedDatabaseBuilder.setType(EmbeddedDatabaseType.H2)
                .addScript("create-db.sql").addScript("insert-data.sql").build();
        return embeddedDatabase;
    }
}
