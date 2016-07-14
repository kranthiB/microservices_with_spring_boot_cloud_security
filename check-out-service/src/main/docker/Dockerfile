FROM frolvlad/alpine-oraclejdk8:slim
ADD check-out-service-1.0.jar check-out-service.jar
RUN sh -c 'touch /check-out-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/check-out-service.jar"]
