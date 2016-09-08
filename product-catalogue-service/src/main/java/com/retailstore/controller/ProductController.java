package com.retailstore.controller;

import com.retailstore.dto.ProductDto;
import com.retailstore.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kranthi on 6/23/2016.
 */
@RestController
@RequestMapping(value = "products")
@Api(value = "Product Controller", description = "This Controller is for the Product Catalogue Service", tags = "Product Controller")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "This Will be used to save a product")
    public void addProduct(@RequestBody final ProductDto productVo) {
        productService.addProduct(productVo);
    }

    @RequestMapping(value = "/searchbyIds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "This will be used to retrieve the list of products based on product ids ")
    public List<ProductDto> getProductsByTypeAndName(@RequestParam(value = "productIds", required = true) final String productIds) {
        String[] stringIds = productIds.split(",");
        long[] ids = Arrays.stream(stringIds).mapToLong(Long::parseLong).toArray();
        return productService.getProductsByIds(ids);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "This will be used to get all the Product details available in the store")
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "This will be used to delete a product based on id")
    public void deleteProduct(@PathVariable final Long id) {
        productService.deleteProduct(id);
    }
}
