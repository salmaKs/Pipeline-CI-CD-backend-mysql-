FROM openjdk:8-jdk

WORKDIR /app

COPY  /target/gestion-station-ski-1.0.jar /app/application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
