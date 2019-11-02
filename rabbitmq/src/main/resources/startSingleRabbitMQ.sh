#!/usr/bin/env bash

docker rm -f singleRabbit multiRabbit1 multiRabbit2 multiRabbit3 | true
docker run -d --name singleRabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management