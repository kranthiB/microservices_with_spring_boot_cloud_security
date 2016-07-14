package com.retailstore.config;

import com.retailstore.controller.CheckOutController;
import com.retailstore.dao.ICheckOutDao;
import com.retailstore.dao.impl.CheckOutDaoImpl;
import com.retailstore.service.ICheckOutService;
import com.retailstore.service.impl.CheckOutServiceImpl;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
    public CheckOutController checkOutController(final LoadBalancerClient loadBalancerClient, final ICheckOutService checkOutService) {
        return new CheckOutController(loadBalancerClient, checkOutService);
    }

    @Bean
    public ICheckOutService checkOutService(final ICheckOutDao checkOutDao) {
        return new CheckOutServiceImpl(checkOutDao);
    }

    @Bean
    public ICheckOutDao checkOutDao(final JdbcTemplate jdbcTemplate) {
        return new CheckOutDaoImpl(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase embeddedDatabase = embeddedDatabaseBuilder.setType(EmbeddedDatabaseType.H2)
                .addScript("create-db.sql").build();
        return embeddedDatabase;
    }
}
