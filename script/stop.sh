#!/bin/bash

docker-compose -f /home/ec2-user/app/docker-compose-dev.yml down || true

# 안 사용하는 이미지 정리
docker image prune -f