package com.retailstore.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by user on 10-07-2016.
 */
public class ResponseDto {

    private BigDecimal totalTax;

    private BigDecimal totalAmountBase;

    private BigDecimal totalAmount;

    private List<ProductBillDto> itemizedBills;

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public List<ProductBillDto> getItemizedBills() {
        return itemizedBills;
    }

    public void setItemizedBills(List<ProductBillDto> itemizedBills) {
        this.itemizedBills = itemizedBills;
    }

    public BigDecimal getTotalAmountBase() {
        return totalAmountBase;
    }

    public void setTotalAmountBase(BigDecimal totalAmountBase) {
        this.totalAmountBase = totalAmountBase;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

}
