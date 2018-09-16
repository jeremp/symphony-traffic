FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/symphony-traffic*.jar symphony-traffic.jar
CMD java ${JAVA_OPTS} -jar symphony-traffic.jar