FROM openjdk:8
VOLUME /tmp
COPY target/meals-api*.jar app.jar
EXPOSE 8084
EXPOSE 5005
ENV JAVA_OPTS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]