FROM adoptopenjdk/openjdk8:latest
EXPOSE 9002
ADD target/review-service-0.0.1-SNAPSHOT.jar review-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "review-service-0.0.1-SNAPSHOT.jar"]
