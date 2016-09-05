FROM ruimo/dockerfile-ubuntu1404-jdk8
MAINTAINER Kranthi Kumar Bitra<kranthi.b76@gmail.com>
  
RUN apt-get update -qq \
 && apt-get install -qqy curl \
 && apt-get clean

RUN curl -L -O https://download.elastic.co/beats/filebeat/filebeat_1.0.1_amd64.deb \
 && dpkg -i filebeat_1.0.1_amd64.deb \
 && rm filebeat_1.0.1_amd64.deb

ADD filebeat.yml /etc/filebeat/filebeat.yml


RUN mkdir -p /etc/pki/tls/certs
ADD logstash-forwarder.crt /etc/pki/tls/certs/logstash-forwarder.crt


ADD product-catalogue-service-1.0.jar product-catalogue-service.jar
RUN sh -c 'touch /product-catalogue-service.jar'
ADD ./start.sh /usr/local/bin/start.sh
RUN chmod +x /usr/local/bin/start.sh
CMD [ "/usr/local/bin/start.sh" ]
