package az.company.users.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {    public static final String BORROW_CREATED_QUEUE = "borrow.created.queue";
    public static final String BORROW_CREATED_EXCHANGE = "borrow.exchange";
    public static final String BORROW_CREATED_DLQ = "borrow.dlq";
    public static final String BORROW_CREATED_ROUTING_KEY = "borrow.created";
    public static final String BORROW_UPDATE_QUEUE = "borrow.updated.queue";
    public static final String BORROW_UPDATE_EXCHANGE = "borrow.update.exchange";
    public static final String BORROW_UPDATE_DLQ = "borrow.update.dlq";
    public static final String BORROW_UPDATE_ROUTING_KEY = "borrow.updated";
    @Bean
    public Queue borrowCreateQueue() {
        return new Queue(BORROW_CREATED_QUEUE);
    }
    @Bean
    public TopicExchange borrowCreateExchange() {
        return new TopicExchange(BORROW_CREATED_EXCHANGE);
    }
    @Bean
    public Binding borrowBinding() {
        return BindingBuilder
                .bind(borrowCreateQueue())
                .to(borrowCreateExchange())
                .with(BORROW_CREATED_ROUTING_KEY);
    }
    @Bean
    public Queue borrowDlq() {
        return new Queue(BORROW_CREATED_DLQ);
    }

    @Bean
    public TopicExchange borrowDlqExchange() {
        return new TopicExchange(BORROW_CREATED_DLQ);
    }
    @Bean
    public Queue borrowUpdateQueue() {
        return new Queue(BORROW_UPDATE_QUEUE);
    }
    @Bean
    public TopicExchange borrowUpdateExchange() {
        return new TopicExchange(BORROW_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue borrowUpdateDlq() {
        return new Queue(BORROW_UPDATE_DLQ);
    }

    @Bean
    public Binding borrowUpdateBinding() {
        return BindingBuilder
                .bind(borrowUpdateQueue())
                .to(borrowUpdateExchange())
                .with(BORROW_UPDATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
