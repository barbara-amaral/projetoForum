FROM adoptopenjdk/openjdk11:latest
RUN adduser --system --group spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Xmx512m","-Dserver.port=8080" ,"-jar", "/app.jar"]

