FROM openjdk:11-jre-slim
LABEL maintainer=almaviva.it
VOLUME /tmp

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
RUN mkdir -p /app/config

ENV ZEEBE_INSECURE_CONNECTION true

EXPOSE 8080
ENTRYPOINT ["java",  "-cp","app:app/lib/*", "it.almaviva.eai.zeebe.monitor.ZeebeSimpleMonitorApp"]