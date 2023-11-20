FROM maven:3.8.6-eclipse-temurin-18-alpine AS builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package -DskipTests

FROM openjdk:18-alpine

COPY --from=builder target/LibraryService-0.0.1-SNAPSHOT.jar backend-SNAPSHOT.jar

EXPOSE 8060

HEALTHCHECK --interval=5s --timeout=2s --retries=3 CMD wget --no-verbose --tries=1 --spider http://localhost:8060/api/v1/manage/health || exit 1

CMD ["java", "-jar", "backend-SNAPSHOT.jar"]
