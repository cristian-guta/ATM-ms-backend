FROM adoptopenjdk/openjdk8:latest
EXPOSE 9004
ADD target/account-service-0.0.1-SNAPSHOT.jar account-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "account-service-0.0.1-SNAPSHOT.jar"]
