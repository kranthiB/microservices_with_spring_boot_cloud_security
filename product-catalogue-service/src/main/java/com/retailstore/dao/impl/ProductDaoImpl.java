package com.retailstore.dao.impl;

import com.retailstore.dao.IProductDao;
import com.retailstore.dto.ProductDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by kranthi on 23-06-2016.
 */
public class ProductDaoImpl implements IProductDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<ProductDto> findAll() {
        return jdbcTemplate.query("select * from product", productDtoRowMapper);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from product where id= ?", id);

    }

    @Override
    public void save(ProductDto productDto) {
        jdbcTemplate.update("insert into product values (?,?,?,?)",
                productDto.getId(), productDto.getName(), productDto.getCategory(), productDto.getCost());
    }

    @Override
    public List<ProductDto> getProductsByIds(List<Long> productIds) {
        Map<String, List<Long>> paramMap = Collections.singletonMap("productIds", productIds);
        return namedParameterJdbcTemplate.query("select * from product where id in (:productIds) ", paramMap,
                productDtoRowMapper);
    }

    private RowMapper<ProductDto> productDtoRowMapper = new RowMapper<ProductDto>() {
        @Override
        public ProductDto mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductDto productDto = new ProductDto();
            productDto.setId(resultSet.getLong("id"));
            productDto.setName(resultSet.getString("name"));
            productDto.setCategory(resultSet.getString("category"));
            productDto.setCost(resultSet.getBigDecimal("cost"));
            return productDto;
        }
    };
}
