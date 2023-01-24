package com.wandercosta.rabbitmq2019;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryContextWrapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@EnableRabbit
@RestController
@EnableScheduling
@SpringBootApplication
class ApplicationWithMultiRabbitMQ {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWithMultiRabbitMQ.class, args);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ConnectionFactoryContextWrapper contextWrapper;

    @Scheduled(fixedRate = 1000L)
    void publish() {
        final MessageProperties properties = new MessageProperties();
        properties.setHeader("one-header", "some value");
        final Message message = new Message("data".getBytes(StandardCharsets.UTF_8), properties);

        rabbitTemplate.convertAndSend("ex1", "rt1", message);

        rabbitTemplate.convertAndSend("ex1", "rt1", "data");
        contextWrapper.run("broker2",
                () -> rabbitTemplate.convertAndSend("ex2", "rt2",    "data"));
        contextWrapper.run("broker3",
                () -> rabbitTemplate.convertAndSend("ex3", "rt3", "data"));
    }

    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange("ex1"), key = "rt1", value = @Queue("q1")))
    void consume1(String message) {
        System.out.println("RabbitMQ #1: " + message);
    }

    @RabbitListener(containerFactory = "broker2",
            bindings = @QueueBinding(exchange = @Exchange("ex2"), key = "rt2", value = @Queue("q2")))
    void consume2(String message) {
        System.out.println("RabbitMQ #2: " + message);
    }

    @RabbitListener(containerFactory = "broker3",
            bindings = @QueueBinding(exchange = @Exchange("ex3"), key = "rt3", value = @Queue("q3")))
    void consume3(String message) {
        System.out.println("RabbitMQ #3: " + message);
    }
}



