package com.retailstore.dao.impl;

import com.retailstore.dao.ICheckOutDao;
import com.retailstore.dto.ProductBillDto;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by user on 10-07-2016.
 */
public class CheckOutDaoImpl implements ICheckOutDao {

    private final JdbcTemplate jdbcTemplate;

    public CheckOutDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveProductBill(ProductBillDto productBillDto) {
        jdbcTemplate.update("insert into productBill values (?,?,?,?,?,?)",
                productBillDto.getProductBillNo(), productBillDto.getAmountBase(), productBillDto.getTaxRate(),
                productBillDto.getTaxRate(), productBillDto.getAmountTotal(), productBillDto.getProductDto().getId());
    }
}
