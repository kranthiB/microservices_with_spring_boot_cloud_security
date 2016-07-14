FROM frolvlad/alpine-oraclejdk8:slim
ADD config-server-1.0.jar config-server.jar
RUN sh -c 'touch /config-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/config-server.jar"]
