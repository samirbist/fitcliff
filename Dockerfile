FROM eclipse-temurin:17-jdk-alpine

# Expose port 8080
EXPOSE 8080

# Copy the JAR file to the container's working directory
ADD target/fitcliff-0.0.1-SNAPSHOT.jar /app/fitcliff-0.0.1-SNAPSHOT.jar

ENV LOG_DIR=/opt/logs

RUN mkdir -p $LOG_DIR

# Set the working directory
WORKDIR /app

# Run the JAR file
ENTRYPOINT ["java","-jar","fitcliff-0.0.1-SNAPSHOT.jar"]
