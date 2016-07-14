package com.retailstore.service;

import com.retailstore.dto.ProductDto;
import com.retailstore.dto.ResponseDto;

import java.util.List;

/**
 * Created by user on 10-07-2016.
 */
public interface ICheckOutService {

    ResponseDto getItemizedBills(List<ProductDto> products);
}
