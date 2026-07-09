package az.company.books.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class RabbitMQConfig {
    public static final String BORROW_QUEUE = "borrow_queue";
    public static final String BORROW_EXCHANGE = "borrow.exchange";
    public static final String BORROW_ROUTING_KEY = "borrow.created";
    @Bean
    public Queue borrowQueue() {
        return new Queue(BORROW_QUEUE);
    }
    @Bean
    public TopicExchange borrowExchange() {
        return new TopicExchange(BORROW_EXCHANGE);
    }
    @Bean
    public Binding borrowBinding() {
        return BindingBuilder
                .bind(borrowQueue())
                .to(borrowExchange())
                .with("borrow.created");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
