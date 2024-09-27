#Base image for deployment
FROM openjdk:21-slim
ARG JAR_FILE=target/users-api-1.0.jar
#Copying JAR file to the generated container
COPY ${JAR_FILE} users-api.jar
EXPOSE 8080
#Execute JAR when container is initialized
ENTRYPOINT ["java", "-jar","users-api.jar"]