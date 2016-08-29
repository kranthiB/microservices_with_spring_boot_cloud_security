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

## Execution - Continuous Deployment

[Build Continuous Deployment with Jenkins and docker] (https://github.com/kranthiB/Build-Continuous-Integration-with-Jenkins-and-Dokcer/blob/master/README.md)

## Hystrix Internals in One Picture
![hystrix-flow](https://cloud.githubusercontent.com/assets/20100300/18063574/2445416a-6e49-11e6-8937-b45e7cda6af6.png)
