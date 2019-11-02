#!/usr/bin/env bash

docker rm -f singleRabbit multiRabbit1 multiRabbit2 multiRabbit3 | true
docker run -d --name multiRabbit1 -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d --name multiRabbit2 -p 5673:5672 -p 15673:15672 rabbitmq:3-management
docker run -d --name multiRabbit3 -p 5674:5672 -p 15674:15672 rabbitmq:3-management