FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mody-server-0.0.1.jar
ENTRYPOINT ["java","-jar","/mody-server-0.0.1.jar"]