FROM ubuntu:22.04 AS builder
# # BUILD JAR FILE
# # -----------------------------
# Runs on general ubuntu image
# Uses Maven assembly:single
# Compiles all dependencies

RUN apt-get update \
    && apt-get install -y git \
    && apt-get install -y openjdk-17-jdk \
    && apt-get install -y maven

WORKDIR /build
ADD build/src ./src/
ADD build/pom.xml .

RUN mvn compile
RUN mvn assembly:single

# # END BUILD JAR
# # -----------------------------




FROM ubuntu/jre:17-22.04_edge AS environment
# # RUN JAR
# # -----------------------------
# Runs on chiselled Ubuntu image
# Clones the built JAR from builder

WORKDIR /app
COPY --from=builder /build/target/dionysus-latest-jar-with-dependencies.jar .
CMD ["-jar", "dionysus-latest-jar-with-dependencies.jar"]

# # END RUN
# # -----------------------------
