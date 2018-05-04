FROM maven:3-jdk-8 as build

# Install dependencies
RUN apt-get update && apt-get install -y \
    git \
 && rm -rf /var/lib/apt/lists/* \
 && git clone https://github.com/sastix/toolkit.git \
 && cd /toolkit \
 && mvn install -DskipTests

# Build
ADD . /licenser

RUN cd /licenser \
    &&  mvn install

# Create final image
FROM openjdk:8

COPY --from=build /licenser/licenser-server/target /target

WORKDIR /target

ENTRYPOINT ["java", "-Xmx512m", "-Dspring.profiles.active=mysql", "-Dlogging.level.com.sastix.licenser=INFO", "-jar", "server-0.0.1-SNAPSHOT-exec.jar"]

EXPOSE 8585
