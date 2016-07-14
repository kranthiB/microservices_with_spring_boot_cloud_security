FROM frolvlad/alpine-oraclejdk8:slim
ADD client-gateway-1.0.jar client-gateway.jar
RUN sh -c 'touch /client-gateway.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/client-gateway.jar"]
