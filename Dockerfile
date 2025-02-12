FROM openjdk:21-jdk-slim

# 크롬 설치
RUN apt-get update && apt-get install -y wget curl unzip \
    && wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y ./google-chrome-stable_current_amd64.deb \
    && rm ./google-chrome-stable_current_amd64.deb \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# JAR 파일 복사 및 실행
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mody-server-0.0.1.jar
ENTRYPOINT ["java","-jar","/mody-server-0.0.1.jar"]