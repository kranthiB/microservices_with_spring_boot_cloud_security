#!/bin/bash

curl -XPUT 'http://192.168.99.102:9200/_template/filebeat?pretty' -d@/etc/filebeat/filebeat.template.json
/etc/init.d/filebeat start
java -Djava.security.egd=file:/dev/./urandom -jar /product-catalogue-service.jar
