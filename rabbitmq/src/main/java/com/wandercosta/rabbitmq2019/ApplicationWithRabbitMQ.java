package com.wandercosta.rabbitmq2019;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@SpringBootApplication
class ApplicationWithRabbitMQ {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWithRabbitMQ.class, args);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 1000L)
    void publish() {
        rabbitTemplate.convertAndSend("ex1", "rt1", "data");
    }

    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange("ex1"), key = "rt1", value = @Queue("q1")))
    void consume1(String message) {
        System.out.println(message);
    }
}



