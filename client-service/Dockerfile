FROM adoptopenjdk/openjdk8:latest
EXPOSE 9003
ADD target/client-service-0.0.1-SNAPSHOT.jar client-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "client-service-0.0.1-SNAPSHOT.jar"]
