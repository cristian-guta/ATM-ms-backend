FROM adoptopenjdk/openjdk8:latest
EXPOSE 8761
ADD target/server-0.0.1-SNAPSHOT.jar server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
