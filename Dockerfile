FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
RUN --mount=source=pom.xml,target=pom.xml \
    --mount=source=src,target=src \
    --mount=type=cache,target=/root/.m2 \
    mvn package

FROM gcr.io/distroless/java21-debian12:nonroot
ENV PORT 8080
EXPOSE 8080
WORKDIR /app
COPY --from=builder /build/target/*.jar efms.jar
CMD ["efms.jar"]
