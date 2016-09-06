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
  
      docker-machine ls
      
  Create a base directory as “ELK” to initiate the configuration – mkdir ELK
  
  Download the file -  under the ELK directory.
  
  Add the content “subjectAltName = IP: 192.168.99.102” under  “v3_ca” section in “openssl.cnf”
  
  Execute the openssl command to generate the certificate and key which will later be used to have ssl handshake
  between filebeat and logstash.
  
      openssl req -config openssl.cnf -x509 -days 3650 -batch -nodes -newkey rsa:2048 -keyout
      logstash-forwarder.key -out logstash-forwarder.crt
          
  Create a file with “02-beats-input.conf” under “ELK” directory, fill with the below content
  
      input {
        beats {
	        port => 5044
	        ssl => true
	        ssl_certificate => "/etc/pki/tls/certs/logstash-forwarder.crt"
	        ssl_key => "/etc/pki/tls/private/logstash-forwarder.key"
	      }
	    }
  Create a file with name “Dockerfile” under “ELK” directory , fill with the below content
  
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
        o	In order to confirm whether the image got created, execute the command – “docker images myelk”
        o	docker-compose up
        o	In order to confirm whether the process created or not, execute the command “docker ps -a”
            which should be in “Up” status
          
  Connect to the bash by using the command – “docker exec –it ELK_CONTAINER_ID bash”
  
  In that bash , execute the command - “cd ~/beats-dashboards-1.1.0”
  
  Execute the command – “./load.sh”
  
  Now exit the bash by executing the command -  “exit”
  
  In the browser, access the url - http://192.168.99.102:5601/ , now navigate to “Settings->Indices” ,
  will be seen four index patterns like 
  
  Now, select the “filebeat-*” pattern and click the star which will be selected this index as default pattern

## Filebeat Configuration
  Create a docker machine using the below command which will be used to set up Filebeat + Spring Boot.
  
    docker-machine create -d virtualbox --virtualbox-memory "2000" --virtualbox-disk-size "10000" filebeat
      
  Enable the above created machine using the below command so that all the successive commands will be executed
  in this newly created machine.
      
    eval $(docker-machine env filebeat)
      
  To ensure whether the above machine got active or not, execute the below command, under the “Active” column
  value must be “*” for filebeat node.
      
    docker-machine ls
      
  Create a base directory as “filebeat” to initiate the configuration - mkdir filebeat
  
  Download the jar -  under the “filebeat” directory. This is sample spring-boot project which writes the logs 
  to directory  - /var/log/spring and provides the following resources (which will be used to test)
  
        o	GET      /products 
        o	GET      / searchbyIds
        o	POST     / products
        o	DELETE   / products/{id}
        
  Copy the logstash-forwarder.crt file from ELK node to the “filebeat” directory.
  
  Create a file with name “filebeat.yml” under the “filebeat” directory – touch filebeat.yml
  
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
          
          o	In order to confirm whether the images got created, execute the command – “docker images myfilebeat
          
          o	Execute the command – “docker run –p 8870:8870 –p 5400:5400 myfilebeat”
          

## Visualize the logs
  Access the kibana url - http://192.168.99.102:5601/ in the browser
  
  In that, click the discover link, will visualize all the logs related to product-catalogue service 
  
 

