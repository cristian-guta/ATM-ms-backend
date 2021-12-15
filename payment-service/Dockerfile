FROM adoptopenjdk/openjdk8:latest
EXPOSE 9005
ADD target/payment-service-0.0.1-SNAPSHOT.jar payment-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "payment-service-0.0.1-SNAPSHOT.jar"]
