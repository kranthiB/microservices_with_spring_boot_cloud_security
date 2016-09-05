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