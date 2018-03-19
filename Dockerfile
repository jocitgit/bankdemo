FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/bankdemo-0.0.1-SNAPSHOT.jar bankdemo.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bankdemo.jar"]
