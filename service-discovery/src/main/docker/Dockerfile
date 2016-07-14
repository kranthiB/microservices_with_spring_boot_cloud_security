FROM frolvlad/alpine-oraclejdk8:slim
ADD service-discovery-1.0.jar service-discovery.jar
RUN sh -c 'touch /service-discovery.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/service-discovery.jar"]
