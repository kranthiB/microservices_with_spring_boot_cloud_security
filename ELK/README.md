# ELK Set up in Docker ToolBox

## ELK Configuration
  Create a docker machine using the below command which will be used to set up ELK.

![1](https://cloud.githubusercontent.com/assets/20100300/18266268/899b03b0-73df-11e6-9954-5c9555cb8b9a.JPG)

	docker-machine create -d virtualbox --virtualbox-memory "2000" --virtualbox-disk-size "5000" elknode
  
  Enable the above created machine using the below command so that all the successive commands will be executed
  in this newly created machine.
  
![2](https://cloud.githubusercontent.com/assets/20100300/18266267/899a8bec-73df-11e6-904f-dcfce43c755e.JPG)

	eval $(docker-machine env elknode)
	        
  To ensure whether the above machine got active or not, execute the below command, under the “Active” column 
  value must be “*” for elk node

![3](https://cloud.githubusercontent.com/assets/20100300/18266266/899a58fc-73df-11e6-83b5-10e9dfd82151.JPG)
  
	docker-machine ls
      
  Create a base directory as “ELK” to initiate the configuration – mkdir ELK

![4](https://cloud.githubusercontent.com/assets/20100300/18266272/89d0ab14-73df-11e6-9380-c18433e1c65b.JPG)  

  Download the file - [openssl.cnf](https://github.com/kranthiB/microservices_with_spring_boot_cloud_security/blob/master/ELK/openssl.cnf) under the ELK directory.
  
  Add the content “subjectAltName = IP: 192.168.99.102” under  “v3_ca” section in “openssl.cnf”
  
![1](https://cloud.githubusercontent.com/assets/20100300/18268603/1dd06494-73ea-11e6-9f17-33c44cad9e66.jpg) 
  
  Execute the openssl command to generate the certificate and key which will later be used to have ssl handshake
  between filebeat and logstash.

![6](https://cloud.githubusercontent.com/assets/20100300/18266271/89d075ea-73df-11e6-9892-55d425bf6b01.JPG)  
  
	openssl req -config openssl.cnf -x509 -days 3650 -batch -nodes -newkey rsa:2048 -keyout
	logstash-forwarder.key -out logstash-forwarder.crt
          
  Create a file with “02-beats-input.conf” under “ELK” directory, fill with the below content

![7](https://cloud.githubusercontent.com/assets/20100300/18266273/89f89368-73df-11e6-9c43-ad94e02bca03.JPG)    
  
	input {
		beats {
			port => 5044
			ssl => true
	        	ssl_certificate => "/etc/pki/tls/certs/logstash-forwarder.crt"
	        	ssl_key => "/etc/pki/tls/private/logstash-forwarder.key"
	      	}
	}
  Create a file with name “Dockerfile” under “ELK” directory , fill with the below content

![8](https://cloud.githubusercontent.com/assets/20100300/18266275/89ff4a50-73df-11e6-8282-46319fb1cbdb.JPG)  
  
	FROM sebp/elk:latest
	MAINTAINER Kranthi Kumar Bitra <kranthi.b76@gmail.com>

	# LOGSTASH CONFIGURATION
	RUN mkdir -p /etc/pki/tls/certs
	COPY logstash-forwarder.crt /etc/pki/tls/certs/logstash-forwarder.crt
	RUN mkdir -p /etc/pki/tls/private
	COPY logstash-forwarder.key /etc/pki/tls/private/logstash-forwarder.key
	COPY 02-beats-input.conf /etc/logstash/conf.d/02-beats-input.conf

	# KIBANA CONFIGURATION
	RUN cd ~ && { curl -L -O https://download.elastic.co/beats/dashboards/beats-dashboards-1.1.0.zip ; cd -; }
	RUN apt-get -y install unzip
	RUN cd ~ && { unzip beats-dashboards-*.zip  ; cd -; } 
  
  Create a file with name “docker-compose.yml” under “ELK” directory , fill with the below content

![9](https://cloud.githubusercontent.com/assets/20100300/18266274/89ff5252-73df-11e6-9ec1-960ec2573815.JPG)  
  
	myelk:
		image: myelk
		ports:
			- 9200:9200
			- 9300:9300
      	  		- 5044:5044
      	  		- 5000:5000
      	  		- 5601:5601
		volumes:  
			- ./log/elasticsearch:/var/log/elasticsearch
			- ./log/logstash:/var/log/logstash
     			- ./log/kibana:/var/log/kibana
     			
  In command prompt, go to the “ELK” directory where the above five files exists, execute the below commands
  to run the elk
  
	o	docker build –t myelk . 

![10](https://cloud.githubusercontent.com/assets/20100300/18266279/8a289eb4-73df-11e6-9a33-a4b11df237fe.JPG)

	o	In order to confirm whether the image got created, execute the command – “docker images myelk”

![11](https://cloud.githubusercontent.com/assets/20100300/18266277/8a24514c-73df-11e6-9f63-fd585ca3cd13.JPG)

	o	docker-compose up

![12](https://cloud.githubusercontent.com/assets/20100300/18266276/8a242ef6-73df-11e6-8477-3be4cbdb71e0.JPG)

	o	In order to confirm whether the process created or not, execute the command “docker ps -a”
		which should be in “Up” status

![13](https://cloud.githubusercontent.com/assets/20100300/18266278/8a276058-73df-11e6-8522-4484279306f2.JPG)
          
  Connect to the bash by using the command – “docker exec –it ELK_CONTAINER_ID bash”

![14](https://cloud.githubusercontent.com/assets/20100300/18266280/8a2e25c8-73df-11e6-96ec-ebbbf61f6982.JPG)  
  
  In that bash , execute the command - “cd ~/beats-dashboards-1.1.0”

![15](https://cloud.githubusercontent.com/assets/20100300/18266282/8a2f353a-73df-11e6-9fa6-7ea19ca765ab.JPG)  
  
  Execute the command – “./load.sh”

![16](https://cloud.githubusercontent.com/assets/20100300/18266286/8a56b038-73df-11e6-8520-a7a55ee0bcfa.JPG)  
  
  Now exit the bash by executing the command -  “exit”
  
  In the browser, access the url - http://192.168.99.102:5601/ , now navigate to “Settings->Indices” ,
  will be seen four index patterns like 

![17](https://cloud.githubusercontent.com/assets/20100300/18266284/8a53d3d6-73df-11e6-9890-1fbc704fff1f.JPG)  
  
  Now, select the “filebeat-*” pattern and click the star which will be selected this index as default pattern

![18](https://cloud.githubusercontent.com/assets/20100300/18266285/8a56723a-73df-11e6-8565-eddc09cfb56c.JPG)  

## Filebeat Configuration
  Create a docker machine using the below command which will be used to set up Filebeat + Spring Boot.

![19](https://cloud.githubusercontent.com/assets/20100300/18266287/8a5a713c-73df-11e6-9b2d-c62d1a384416.JPG)  
  
	docker-machine create -d virtualbox --virtualbox-memory "2000" --virtualbox-disk-size "10000" filebeat
      
  Enable the above created machine using the below command so that all the successive commands will be executed
  in this newly created machine.

![20](https://cloud.githubusercontent.com/assets/20100300/18266288/8a5c54b6-73df-11e6-8d75-dbc7eb634f58.JPG)  
      
	eval $(docker-machine env filebeat)
      
  To ensure whether the above machine got active or not, execute the below command, under the “Active” column
  value must be “*” for filebeat node.
  
![21](https://cloud.githubusercontent.com/assets/20100300/18266289/8a5dd08e-73df-11e6-9aaf-fd426c3ca266.JPG)  
      
	docker-machine ls
      
  Create a base directory as “filebeat” to initiate the configuration - mkdir filebeat

![22](https://cloud.githubusercontent.com/assets/20100300/18266290/8a8306d8-73df-11e6-8719-f3bc5e40aff4.JPG)  
  
  Download the jar - [product-catalogue-service-1.0.jar] (https://github.com/kranthiB/microservices_with_spring_boot_cloud_security/blob/master/ELK/filebeat/product-catalogue-service-1.0.jar) under the “filebeat” directory. This is sample spring-boot project which writes the logs 
  to directory  - /var/log/spring and provides the following resources (which will be used to test)
  
	o	GET      /products 
	o	GET      / searchbyIds
	o	POST     / products
	o	DELETE   / products/{id}
        
  Copy the logstash-forwarder.crt file from ELK node to the “filebeat” directory.
  
  Create a file with name “filebeat.yml” under the “filebeat” directory – touch filebeat.yml

![23](https://cloud.githubusercontent.com/assets/20100300/18266291/8a84f2c2-73df-11e6-8997-2699738cfffb.JPG)  
  
  Fill the filebeat.yml with the below the content

	output:
		logstash:
			enabled: true
			hosts:
				- 192.168.99.102:5044
			timeout: 15
		tls:
			certificate_authorities:
				- /etc/pki/tls/certs/logstash-forwarder.crt
	filebeat:
		prospectors:
			-
				paths:
					- /var/log/spring/*.log
				document_type: product-service-log
                
  Create a file with name “Dockerfile” and fill with the below content

![24](https://cloud.githubusercontent.com/assets/20100300/18266292/8a86d79a-73df-11e6-9be9-687d284ce265.JPG)  
  
	FROM ruimo/dockerfile-ubuntu1404-jdk8
	MAINTAINER Kranthi Kumar Bitra<kranthi.b76@gmail.com>
  
	RUN apt-get update -qq \
		&& apt-get install -qqy curl \
		&& apt-get clean

	RUN curl -L -O https://download.elastic.co/beats/filebeat/filebeat_1.0.1_amd64.deb 
		&& dpkg -i filebeat_1.0.1_amd64.deb && rm filebeat_1.0.1_amd64.deb

	ADD filebeat.yml /etc/filebeat/filebeat.yml

	RUN mkdir -p /etc/pki/tls/certs
	ADD logstash-forwarder.crt /etc/pki/tls/certs/logstash-forwarder.crt

	ADD product-catalogue-service-1.0.jar product-catalogue-service.jar
	RUN sh -c 'touch /product-catalogue-service.jar'

	ADD ./start.sh /usr/local/bin/start.sh
	RUN chmod +x /usr/local/bin/start.sh
	CMD [ "/usr/local/bin/start.sh" ]
          
  Create a file with name “start.sh” and fill with the below content


  
	curl -XPUT 'http://192.168.99.102:9200/_template/filebeat?pretty' -d@/etc/filebeat/filebeat.template.json
	/etc/init.d/filebeat start
	java -Djava.security.egd=file:/dev/./urandom -jar /product-catalogue-service.jar
          
  In the command prompt, go to the directory where the above five files exist
  
	o	docker build –t myfilebeat .
	
![25](https://cloud.githubusercontent.com/assets/20100300/18266293/8a8913de-73df-11e6-9b2e-b921844c8769.JPG)
          
          o	In order to confirm whether the images got created, execute the command – “docker images myfilebeat

![26](https://cloud.githubusercontent.com/assets/20100300/18266294/8a8b8e34-73df-11e6-9863-a57323333c37.JPG)	          
          
          o	Execute the command – “docker run –p 8870:8870 –p 5400:5400 myfilebeat”
          

## Visualize the logs
  Access the kibana url - http://192.168.99.102:5601/ in the browser
  
  In that, click the discover link, will visualize all the logs related to product-catalogue service 

![27](https://cloud.githubusercontent.com/assets/20100300/18266295/8a8c222c-73df-11e6-9113-279d27fa1b0b.JPG)  
 

