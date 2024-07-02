FROM amazoncorretto:17-alpine-jdk
COPY stats-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]