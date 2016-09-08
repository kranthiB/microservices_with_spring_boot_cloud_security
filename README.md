# Microservice Pattern

This project demonstrates Microservice Architecture pattern using Spring Boot, Spring Cloud, Spring Secuirty and Docker.

## Infrastructure

![infrastructure](https://cloud.githubusercontent.com/assets/20100300/17996511/0c23cf2e-6b2f-11e6-80b3-4870e0fa2ff7.png)

## Eureka (Service Discovery)
	This will maintain the all instances of business services, client API gateway and also all other instances
	of eureka service discovery in the cluster.
	
## Config Server
	This will have the list of all instances of eureka service discovery which will be maintained in any revision
	control system.  Here, we used “github” so that we can dynamically change eureka server instance in case of any
	modification in the service discovery cluster. No need to re-start the cluster, dynamically cluster will get 
	refreshed on modification in “github
	
## Client API Gateway (ZUUL Proxy)
	Any request from client will be re-directed to actual business through ZUUL Proxy. The following steps involved
	in redirecting to the actual business service url.
			
		1. ZUUL contacts the “Config Server” to know the instances of the Service Discovery.
		2. ZUUL will be provided the actual business service url by the Eureka. Here, Eureka will perform load 
		   balance using “Ribbon” before providing the actual url.
		3. Finally, ZUUL will contact the actual business service url and redirect the response of the respective 
		   service to the requested client

## HYSTRIX
	We can monitor all the requests to the business services by using the “Hystrix Turbine Stream”. This will give
	clear picture of all the requests information like how many got passed, how many got failed, how many still 
	processing etc. for a specific period of time.


## Authorization (SSO) Flow

![authorization-code-flow](https://cloud.githubusercontent.com/assets/20100300/17996681/f637ddda-6b2f-11e6-9608-8927929ddf1a.png)


## Functional Services
### 1.Product Catalogue Service

	It offers the following functionality by using Embedded H2 Database.
    
            1. GET  	/productCatalogueService/products – gives the list of all products
            2. GET 		/productCatalogueService/searchbyIds  – gives the list of products for matching name,type
            4. POST  	/productCatalogueService/products – saves the given product
            5. DELETE 	/productCatalogueService/products/{id} – delete the given product

### 2. Check Out Service

	It offers the following functionality by using Embedded H2 Database.
    
    		1. GET  	/pricingService/products/checkOut/generateItemizedBill
            
    The above method generates the itemized bill for the requested products. This method first
    contact the “Product Catalogue Service” to get the product details for given product id (This has been
    called using “load-balancer-client”). Then, calcaulate the bill for the product list.
    
    
## MicroServices Dashboard

![micorservicedashboard](https://cloud.githubusercontent.com/assets/20100300/17996875/f062b5f0-6b30-11e6-8165-eca4b66dc83f.PNG)

## Monitor Dashboard

![1](https://cloud.githubusercontent.com/assets/20100300/18051542/d28a1962-6e12-11e6-8e22-29a79c14516d.png)

## ELK - LOG Analysis

### Infrastructure
![elk](https://cloud.githubusercontent.com/assets/20100300/18265462/7d2c2338-73db-11e6-88a1-7998fb79a833.png)
### Set-Up
[ELK Set up in Docker Toolbox] (https://github.com/kranthiB/microservices_with_spring_boot_cloud_security/blob/master/ELK/README.md)
### Kibana - UI
![ui](https://cloud.githubusercontent.com/assets/20100300/18250653/66cdc2a6-73a2-11e6-8dc9-c012da9c0730.PNG)

## Swagger API Documentation
![1234](https://cloud.githubusercontent.com/assets/20100300/18349249/f6009e04-7595-11e6-9a79-99fceb70f144.png)

## Execution - Continuous Deployment

[Build Continuous Deployment with Jenkins and docker] (https://github.com/kranthiB/Build-Continuous-Integration-with-Jenkins-and-Dokcer/blob/master/README.md)

## Hystrix Internals

### High Level View
![hystrix-flow](https://cloud.githubusercontent.com/assets/20100300/18075347/9950b4f6-6e91-11e6-8823-d508957f4974.png)

### Request Caching Mechanism
![request-cache](https://cloud.githubusercontent.com/assets/20100300/18075410/3068b096-6e92-11e6-87ec-20508a732dff.png)

### Hystrix Circuit Breaker
![circuit-breaker](https://cloud.githubusercontent.com/assets/20100300/18204061/474f6e8a-70e0-11e6-8ef2-e10ed07df3a7.png)

### Hystrix Isolation
