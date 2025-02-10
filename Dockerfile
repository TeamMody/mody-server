FROM openjdk:21

# 크롬 설치
RUN yum update -y && yum install -y wget curl unzip \
    && wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm \
    && yum install -y ./google-chrome-stable_current_x86_64.rpm \
    && rm ./google-chrome-stable_current_x86_64.rpm \
    && yum clean all

# JAR 파일 복사 및 실행
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mody-server-0.0.1.jar
ENTRYPOINT ["java","-jar","/mody-server-0.0.1.jar"]