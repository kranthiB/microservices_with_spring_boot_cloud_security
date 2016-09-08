package com.retailstore.controller;

import com.google.common.base.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bkranthi on 08-09-2016.
 */
@Controller
@RequestMapping("/swagger-resources")
public class SwaggerController {

    private LoadBalancerClient loadBalancerClient;
    private DiscoveryClient discoveryClient;
    @Autowired(required = false)
    private UiConfiguration uiConfiguration;
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    public SwaggerController(LoadBalancerClient loadBalancerClient, DiscoveryClient discoveryClient) {
        this.loadBalancerClient = loadBalancerClient;
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping(value = "/configuration/security")
    @ResponseBody
    ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return new ResponseEntity<SecurityConfiguration>(
                Optional.fromNullable(securityConfiguration).or(SecurityConfiguration.DEFAULT), HttpStatus.OK);
    }

    @RequestMapping(value = "/configuration/ui")
    @ResponseBody
    ResponseEntity<UiConfiguration> uiConfiguration() {
        return new ResponseEntity<UiConfiguration>(
                Optional.fromNullable(uiConfiguration).or(UiConfiguration.DEFAULT), HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<SwaggerResource>> getSwaggerResources(
            @RequestHeader(value = "authorization", required = false) String authorization) {
        System.out.println("authorization is" + authorization);
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> servicesList = discoveryClient.getServices();
        if (!CollectionUtils.isEmpty(servicesList)) {
            String url = null;
            /*SwaggerResource swaggerResource = resource("productCatalogueServiceGroup", "http://localhost:8765/productCatalogueService/v2/api-docs");
            swaggerResource.setSwaggerVersion("2.0");
            resources.add(swaggerResource);
            swaggerResource = resource("checkOutServiceGroup", "http://localhost:8765/checkOutService/v2/api-docs");
            swaggerResource.setSwaggerVersion("2.0");
            resources.add(swaggerResource);
            for (String serviceName : servicesList) {
                System.out.println("serviceName" + serviceName);
            }*/
            for (String serviceName : servicesList) {
                url = "http://localhost:8765/";
                url = url + serviceName + "/v2/api-docs";
                SwaggerResource swaggerResource = resource(serviceName + "group", url);
                System.out.println("Location is- " + swaggerResource.getLocation() + ",Name  is- " + swaggerResource.getName());
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            }
        } else {
            System.out.println("Services list is empty");
        }
        Collections.sort(resources);
        return new ResponseEntity<List<SwaggerResource>>(resources, HttpStatus.OK);
    }

    private SwaggerResource resource(String swaggerGroup, String baseUrl) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(swaggerGroup);
        swaggerResource.setLocation(swaggerLocation(baseUrl, swaggerGroup));
        return swaggerResource;
    }

    private String swaggerLocation(String swaggerUrl, String swaggerGroup) {
        String base = Optional.of(swaggerUrl).get();
        if (Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
            return base;
        }
        return base + "?group=" + swaggerGroup;
    }
}
