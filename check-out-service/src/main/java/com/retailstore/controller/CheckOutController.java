package com.retailstore.controller;

import com.retailstore.dto.ProductDto;
import com.retailstore.dto.ResponseDto;
import com.retailstore.service.ICheckOutService;
import com.retailstore.util.HeaderRequestInterceptor;
import com.retailstore.util.ServiceName;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 10-07-2016.
 */
@RestController
@RequestMapping(value = "products/checkOut")
public class CheckOutController {

    private final ICheckOutService checkOutService;


    private final LoadBalancerClient loadBalancerClient;

    public CheckOutController(LoadBalancerClient loadBalancerClient, ICheckOutService checkOutService) {
        this.checkOutService = checkOutService;
        this.loadBalancerClient = loadBalancerClient;
    }

    @RequestMapping(value = "/generateItemizedBill", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto getPrice(@RequestParam(value = "productIds", required = true) final String productIds,
                                @RequestHeader(value = "authorization", required = false) String authorization) {
        String[] stringIds = productIds.split(",");
        long[] ids = Arrays.stream(stringIds).mapToLong(Long::parseLong).toArray();
        ServiceInstance serviceInstance = loadBalancerClient.choose(ServiceName.PRODUCT_CATALOGUE_SERVICE.getName());
        URI uri = serviceInstance.getUri();
        String url = uri.toString() + "/products/searchbyIds?productIds=" + productIds;

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("authorization", authorization));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        ProductDto[] productDtos = restTemplate.getForObject(url, ProductDto[].class);
        return checkOutService.getItemizedBills(Arrays.asList(productDtos));
    }
}
