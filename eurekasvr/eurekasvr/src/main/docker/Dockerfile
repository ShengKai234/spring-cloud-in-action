FROM openjdk:17-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/configserver
ARG JAR_FILE
ARG SHELL_FILE
COPY ${JAR_FILE} /usr/local/configserver/app.jar
COPY ${SHELL_FILE} /usr/local/configserver/run.sh
RUN chmod +x /usr/local/configserver/*
CMD /usr/local/configserver/run.sh
# Please note that the JCE policy files are no longer required for Java 17, so the corresponding commands have been removed from the Dockerfile.






