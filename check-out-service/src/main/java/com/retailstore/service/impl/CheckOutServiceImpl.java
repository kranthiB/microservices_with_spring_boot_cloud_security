package com.retailstore.service.impl;

import com.retailstore.dao.ICheckOutDao;
import com.retailstore.dto.ProductBillDto;
import com.retailstore.dto.ProductDto;
import com.retailstore.dto.ResponseDto;
import com.retailstore.service.ICheckOutService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 10-07-2016.
 */
public class CheckOutServiceImpl implements ICheckOutService {

    private final ICheckOutDao checkOutDao;

    private static Long productBillSeqNo = 1L;

    private static Map<String, Integer> taxRateMap = new HashMap<>();

    static {
        taxRateMap.put("A", 10);
        taxRateMap.put("B", 20);
        taxRateMap.put("C", 0);
    }

    public CheckOutServiceImpl(ICheckOutDao checkOutDao) {
        this.checkOutDao = checkOutDao;
    }

    @Override
    public ResponseDto getItemizedBills(List<ProductDto> products) {
        ResponseDto responseDto = new ResponseDto();
        List<ProductBillDto> productBillDtos = new ArrayList<>();
        ProductBillDto productBillDto = null;
        BigDecimal taxAmount = null;
        BigDecimal totalTaxAmount = new BigDecimal(0);
        BigDecimal totalAmountBase = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0);
        for (ProductDto productDto : products) {
            productBillDto = new ProductBillDto();
            productBillDto.setProductBillNo(productBillSeqNo);
            productBillDto.setAmountBase(productDto.getCost());
            productBillDto.setTaxRate(new BigDecimal(taxRateMap.get(productDto.getCategory())));
            taxAmount = getTaxAmount(productBillDto.getAmountBase(), productDto.getCategory());
            productBillDto.setTax(taxAmount);
            productBillDto.setAmountTotal(productBillDto.getAmountBase().add(taxAmount));
            productBillSeqNo++;
            productBillDto.setProductDto(productDto);
            checkOutDao.saveProductBill(productBillDto);
            productBillDtos.add(productBillDto);
            totalTaxAmount = totalTaxAmount.add(taxAmount);
            totalAmountBase = totalAmountBase.add(productBillDto.getAmountBase());
            totalAmount = totalAmount.add(productBillDto.getAmountTotal());
        }
        responseDto.setItemizedBills(productBillDtos);
        responseDto.setTotalAmount(totalAmount);
        responseDto.setTotalAmountBase(totalAmountBase);
        responseDto.setTotalTax(totalTaxAmount);
        return responseDto;
    }

    private BigDecimal getTaxAmount(BigDecimal amountBase, String category) {
        BigDecimal taxAmount = null;
        MathContext mathContext = new MathContext(2);
        BigDecimal tax = new BigDecimal(taxRateMap.get(category) / 100D, mathContext);
        taxAmount = tax.multiply(amountBase);
        return taxAmount;
    }
}
