FROM frolvlad/alpine-oraclejdk8:slim
ADD authorization-service-1.0.jar authorization-service.jar
RUN sh -c 'touch /authorization-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authorization-service.jar"]
