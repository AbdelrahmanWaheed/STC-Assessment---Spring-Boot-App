FROM openjdk:8
COPY ./target/filesystem-1.0.0.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]