FROM amazoncorretto:17 AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test --no-daemon

FROM amazoncorretto:17-alpine as runtime
WORKDIR /app


RUN mkdir -p /app/uploads && \
    mkdir -p /app/uploads/property-images

RUN addgroup -S spring && adduser -S spring -G spring

RUN chown -R spring:spring /app/uploads

COPY --from=build --chown=spring:spring /app/build/libs/*.jar app.jar

USER spring:spring

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
