#!/bin/bash

# 컨테이너 중지
docker-compose -f /home/ec2-user/app/docker-compose-dev.yml stop backend
docker-compose -f /home/ec2-user/app/docker-compose-dev.yml rm -f backend


# 안 사용하는 이미지 정리
docker image prune -f