package az.company.users.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String BORROW_HISTORY_QUEUE = "borrow.created.queue";
    public static final String BORROW_EXCHANGE = "borrow.exchange";
    public static final String BORROW_DLQ = "borrow.dlq";
    public static final String BORROW_ROUTING_KEY = "borrow.created";

    @Bean
    public TopicExchange borrowExchange() {
        return new TopicExchange(BORROW_EXCHANGE);
    }

    @Bean
    public Queue borrowHistoryQueue() {
        return QueueBuilder
                .durable(BORROW_HISTORY_QUEUE)
                .withArgument("x-dead-letter-exchange", BORROW_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", BORROW_DLQ)
                .build();
    }

    @Bean
    public Queue dlq() {
        return new Queue(BORROW_DLQ);
    }

    @Bean
    public Binding borrowHistoryBinding() {
        return BindingBuilder
                .bind(borrowHistoryQueue())
                .to(borrowExchange())
                .with("borrow.created");

    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
