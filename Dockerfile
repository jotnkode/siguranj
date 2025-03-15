FROM chainguard/jdk:latest-dev
USER 0:0
COPY build/libs/siguranj-0.0.1-SNAPSHOT.jar siguranj.jar

RUN apk add sqlite

EXPOSE 8080

USER java:java
COPY schema.sql .
RUN sqlite3 siguranj.db < schema.sql && sqlite3 .tables
ENTRYPOINT [ "java", "-Xms256m", "-Xmx256m", "-jar", "siguranj.jar"]
